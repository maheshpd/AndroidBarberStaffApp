package com.example.androidbarberstaffapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.example.androidbarberstaffapp.Common.Common;
import com.example.androidbarberstaffapp.Interface.IBarberServicesLoadListener;
import com.example.androidbarberstaffapp.Interface.IBottomSheetDialogOnDismissListener;
import com.example.androidbarberstaffapp.Interface.IOnShoppingItemSelected;
import com.example.androidbarberstaffapp.R;
import com.example.androidbarberstaffapp.fragment.ShoppingFragment;
import com.example.androidbarberstaffapp.fragment.TotalPriceFragment;
import com.example.androidbarberstaffapp.model.BarberServices;
import com.example.androidbarberstaffapp.model.ShoppingItem;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class DoneServicesActivity extends AppCompatActivity implements IBarberServicesLoadListener, IOnShoppingItemSelected, IBottomSheetDialogOnDismissListener {

    private static final int MY_CAMERA_REQUEST_CODE = 1000;
    @BindView(R.id.txt_customer_name)
    TextView txt_customer_name;

    @BindView(R.id.txt_customer_phone)
    TextView txt_customer_phone;

    @BindView(R.id.chip_group_services)
    ChipGroup chip_group_services;

    @BindView(R.id.chip_group_shopping)
    ChipGroup chip_group_shopping;

    @BindView(R.id.edt_services)
    AppCompatAutoCompleteTextView edt_services;

    @BindView(R.id.img_customer_hair)
    ImageView img_customer_hair;

    @BindView(R.id.add_shopping)
    ImageButton btn_shopping;

    @BindView(R.id.btn_finish)
    Button btn_finish;

    @BindView(R.id.rdi_no_picture)
    RadioButton rdi_no_picture;

    @BindView(R.id.rdi_picture)
    RadioButton rdi_picture;

    AlertDialog dialog;

    IBarberServicesLoadListener iBarberServicesLoadListener;

    HashSet<BarberServices> servicesAdded = new HashSet<>();
    List<ShoppingItem> shoppingItems = new ArrayList<>();

    LayoutInflater inflater;

    Uri fileUri;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_services);

        ButterKnife.bind(this);

        init();

        initView();

        setCustomerInformation();

        loadBarberServices();
    }

    private void initView() {

        rdi_picture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    img_customer_hair.setVisibility(View.VISIBLE);
                    btn_finish.setEnabled(false);
                }
            }
        });

        rdi_no_picture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    img_customer_hair.setVisibility(View.GONE);
                    btn_finish.setEnabled(true);
                }
            }
        });

        getSupportActionBar().setTitle("Checkout");

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rdi_no_picture.isChecked()) {
                    dialog.dismiss();
                    TotalPriceFragment fragment = TotalPriceFragment.getInstance(DoneServicesActivity.this);
                    Bundle bundle = new Bundle();
                    bundle.putString(Common.SERVICES_ADDED, new Gson().toJson(servicesAdded));
                    bundle.putString(Common.SHOPPING_LIST, new Gson().toJson(shoppingItems));
                    fragment.setArguments(bundle);
                    fragment.show(getSupportFragmentManager(), "Price");
                } else {
                    uploadPicture(fileUri);
                }
            }
        });

        img_customer_hair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
//
                fileUri = getOutputMediaFileUri();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);
            }
        });

        btn_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingFragment shoppingFragment = ShoppingFragment.getInstance(DoneServicesActivity.this);
                shoppingFragment.show(getSupportFragmentManager(), "Shopping");
            }
        });
    }

    private void uploadPicture(Uri fileUri) {
        if (fileUri != null) {
            dialog.show();
            String fileName = Common.getFileName(getContentResolver(), fileUri);
            String path = new StringBuilder("Customer_Pictures/")
                    .append(fileName)
                    .toString();
            storageReference = FirebaseStorage.getInstance().getReference(path);

            UploadTask uploadTask = storageReference.putFile(fileUri);

            //Create task
            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful())
                        Toast.makeText(DoneServicesActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        String url = task.getResult().toString()
                                .substring(0,
                                        task.getResult().toString().indexOf("&token"));

                        dialog.dismiss();

                        TotalPriceFragment fragment = TotalPriceFragment.getInstance(DoneServicesActivity.this);
                        Bundle bundle = new Bundle();
                        bundle.putString(Common.SERVICES_ADDED, new Gson().toJson(servicesAdded));
                        bundle.putString(Common.SHOPPING_LIST, new Gson().toJson(shoppingItems));
                        bundle.putString(Common.IMAGE_DOWNLOADABLE_URL, url);
                        fragment.setArguments(bundle);
                        fragment.show(getSupportFragmentManager(), "Price");


                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(DoneServicesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Image is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getOutputMediaFileUri() {
//        return Uri.fromFile(getOutputMediaFile());
        return Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BarberStaffApp ");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdir())
                return null;
        }

        String time_stamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"
                + time_stamp + "_" + new Random().nextInt() + ".jpg");
        return mediaFile;
    }


    private void init() {
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false)
                .build();

        inflater = LayoutInflater.from(this);
        iBarberServicesLoadListener = this;
    }

    private void loadBarberServices() {
        dialog.show();

        FirebaseFirestore.getInstance()
                .collection("AllSalon")
                .document(Common.state_name)
                .collection("Branch")
                .document(Common.selected_salon.getSalonId())
                .collection("Services")
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iBarberServicesLoadListener.onBarberServicesLoadFailed(e.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<BarberServices> barberServices = new ArrayList<>();
                    for (DocumentSnapshot barberSnapshort : task.getResult()) {
                        BarberServices services = barberSnapshort.toObject(BarberServices.class);
                        barberServices.add(services);
                    }
                    iBarberServicesLoadListener.onBarberServicesLoadSuccess(barberServices);
                }
            }
        });
    }

    private void setCustomerInformation() {
        txt_customer_name.setText(Common.currentBookingInformation.getCustomeName());
        txt_customer_phone.setText(Common.currentBookingInformation.getCustomerPhone());
    }

    @Override
    public void onBarberServicesLoadSuccess(List<BarberServices> barberServicesList) {
        List<String> nameServices = new ArrayList<>();
        //Sort alpha-bet
        Collections.sort(barberServicesList, new Comparator<BarberServices>() {
            @Override
            public int compare(BarberServices barberServices, BarberServices t1) {
                return barberServices.getName().compareTo(t1.getName());
            }
        });

        //Add all name of services after sort
        for (BarberServices barberServices : barberServicesList)
            nameServices.add(barberServices.getName());

        //Create Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, nameServices);
        edt_services.setThreshold(1); //will start working from first character
        edt_services.setAdapter(adapter);
        edt_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Add to chip Group
                int index = nameServices.indexOf(edt_services.getText().toString().trim());

                if (!servicesAdded.contains(barberServicesList.get(index))) {
                    servicesAdded.add(barberServicesList.get(index)); //We don't want to have duplicate services in list so we use HashSet
                    Chip item = (Chip) inflater.inflate(R.layout.chip_item, null);
                    item.setText(edt_services.getText().toString());
                    item.setTag(index);
                    edt_services.setText("");

                    item.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            chip_group_services.removeView(view);
                            servicesAdded.remove(item.getTag());
                        }
                    });

                    chip_group_services.addView(item);
                } else {
                    edt_services.setText("");
                }
            }
        });

        dialog.dismiss();
    }

    @Override
    public void onBarberServicesLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onShoppingItemSelected(ShoppingItem shoppingItem) {
        //Here we will create an List to hold Shopping item
        shoppingItems.add(shoppingItem);
        Log.d("ShoppingItem ", "" + shoppingItems.size());

        Chip item = (Chip) inflater.inflate(R.layout.chip_item, null);
        item.setText(shoppingItem.getName());
        item.setTag(shoppingItems.indexOf(shoppingItem));
        edt_services.setText("");

        item.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chip_group_shopping.removeView(view);
                servicesAdded.remove(item.getTag());
            }
        });

        chip_group_shopping.addView(item);
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                ExifInterface ei = null;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                    ei = new ExifInterface(getContentResolver().openInputStream(fileUri));

                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    Bitmap rotationBitmap = null;
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotationBitmap = rotationImage(bitmap, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotationBitmap = rotationImage(bitmap, 180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotationBitmap = rotationImage(bitmap, 270);
                            break;
                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotationBitmap = bitmap;
                            break;
                    }

                    img_customer_hair.setImageBitmap(rotationBitmap);
                    btn_finish.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap rotationImage(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public void onDismissBottomSheetDialog(boolean fromButton) {
        if (fromButton) // == true
            finish();
    }
}
