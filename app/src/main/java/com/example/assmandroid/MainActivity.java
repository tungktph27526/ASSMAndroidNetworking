package com.example.assmandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ProductAdapter.Callback {
    private RecyclerView idRcv;
    private ProductAdapter productAdapter;
    private List<ProductModel> productList;
    private FloatingActionButton btnShowDialogAddProduct;

    private EditText edTen;
    private EditText edGia;
    private EditText edMota;
    private EditText edAnh;
    private Button btnThem;
    private Button btnHuy;
    private Button btnSua;
    private Button btnXoa;
    private TextView listPr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idRcv = (RecyclerView) findViewById(R.id.id_rcv);




        btnShowDialogAddProduct = (FloatingActionButton) findViewById(R.id.btn_showDialogAddProduct);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        idRcv.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        idRcv.addItemDecoration(itemDecoration);
        ShowProduct();

        btnShowDialogAddProduct.setOnClickListener(this);
        listPr = (TextView) findViewById(R.id.tv_list);

//        productAdapter = new ProductAdapter(new ArrayList<>(), this);
//        idRcv.setAdapter(productAdapter);


    }

    private void ShowProduct() {
        Call<List<ProductModel>> call = ApiService.apiService.getProducts();
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    List<ProductModel> productList = response.body();
                   showList(productList);
                } else {
                    Log.e("API Error", "Failed to fetch products");
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }

    private void showList(List<ProductModel> list) {
        if (list != null){
            productAdapter = new ProductAdapter(list, this);
            idRcv.setAdapter(productAdapter);
            listPr.setText("so san pham: "+String.valueOf(list.size()));
//            productAdapter.setProductList(productList);
//            productAdapter.notifyDataSetChanged();
        }
    }


    private void AddProduct(String name, int price, String description, String image) {
        ProductModel model = new ProductModel();
        model.setName(name);
        model.setPrice(price);
        model.setDescription(description);
        model.setImage(image);
        Call<List<ProductModel>> call = ApiService.apiService.addProduct(model);
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Thêm dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    ShowProduct();
                } else {
                    Toast.makeText(MainActivity.this, "thêm that bai", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Failed to fetch products");
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(MainActivity.this, "lỗi ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateProduct(String id, String name, int price, String description, String image) {
        ProductModel model = new ProductModel();
        model.setName(name);
        model.setPrice(price);
        model.setDescription(description);
        Call<List<ProductModel>> call = ApiService.apiService.updateProduct(id, model);
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "sua thanh cong", Toast.LENGTH_SHORT).show();
                    ShowProduct();
                } else {
                    Toast.makeText(MainActivity.this, "sua that bai", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Failed to fetch products");
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }

    private void DeleteProduct(String id) {
        Call<List<ProductModel>> call = ApiService.apiService.deleteProduct(id);
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "xoa thanh cong", Toast.LENGTH_SHORT).show();
                    ShowProduct();
                } else {
                    Toast.makeText(MainActivity.this, "xoa that bai", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Failed to fetch products");
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }

        });
//        call.enqueue(new Callback<List<ProductModel>>() {
//            @Override
//            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
//                    List<ProductModel> tableItems = response.body();
//                    if (tableItems != null) {
//                        productAdapter.notifyDataSetChanged();
//                    }
//                    //callApiGetTableList();
//                } else {
//                    Log.d("MAIN", "Respone Fail" + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
//                Log.d("MAIN", "Respone Fail" + t.getMessage());
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (dialog != null && dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        edTen = (EditText) dialog.findViewById(R.id.ed_ten);
        edGia = (EditText) dialog.findViewById(R.id.ed_gia);
        edMota = (EditText) dialog.findViewById(R.id.ed_mota);
        edAnh = (EditText) dialog.findViewById(R.id.ed_anh);
        btnThem = (Button) dialog.findViewById(R.id.btn_them);
        btnHuy = (Button) dialog.findViewById(R.id.btn_huy);
        btnThem.setOnClickListener(v ->{
            String name = edTen.getText().toString().trim();
            String price = edGia.getText().toString().trim();
            String description = edMota.getText().toString().trim();
            String image = edAnh.getText().toString().trim();
            if (name.isEmpty() || price.isEmpty() || description.isEmpty() || image.isEmpty()){
                Toast.makeText(this, "ko dc de trong", Toast.LENGTH_SHORT).show();
           }else {
                AddProduct(name, Integer.parseInt(price), description, image);
            }
            dialog.dismiss();
        });
        btnHuy.setOnClickListener(v ->{
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void Edit(ProductModel productModel) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sua_product);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (dialog != null && dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        edTen = (EditText) dialog.findViewById(R.id.ed_ten);
        edGia = (EditText) dialog.findViewById(R.id.ed_gia);
        edMota = (EditText) dialog.findViewById(R.id.ed_mota);
        edAnh = (EditText) dialog.findViewById(R.id.ed_anh);
        btnSua = (Button) dialog.findViewById(R.id.btn_sua);
        btnHuy = (Button) dialog.findViewById(R.id.btn_huy);
        edTen.setText(productModel.getName());
        edGia.setText(String.valueOf(productModel.getPrice()));
        edMota.setText(productModel.getDescription());
        edAnh.setText(productModel.getImage());
        btnSua.setOnClickListener(v ->{
            String name = edTen.getText().toString().trim();
            int price = Integer.parseInt(edGia.getText().toString().trim());
            String description = edMota.getText().toString().trim();
            String image = edMota.getText().toString().trim();
            UpdateProduct(productModel.getId(), name, price, description, image);
            dialog.dismiss();
        });
        btnHuy.setOnClickListener(v ->{
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void Delete(ProductModel productModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa sản phẩm");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            DeleteProduct(productModel.getId());
        });
        builder.setNegativeButton("Hủy", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}