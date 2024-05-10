package exemple.com.simple_phone;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

public class KontactActivity extends AppCompatActivity {
    private Button buttonAddContact;
    private RecyclerView recyclerViewContacts;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kontact);

        buttonAddContact = findViewById(R.id.buttonAddContact);
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);

        // Khởi tạo ContactAdapter
        contactAdapter = new ContactAdapter(this);
        recyclerViewContacts.setAdapter(contactAdapter);

        // Thiết lập layout manager cho RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewContacts.setLayoutManager(layoutManager);

        // Thực hiện lấy dữ liệu từ cơ sở dữ liệu và thêm vào Adapter
        DBHelper dbHelper = new DBHelper(this);
        List<Contact> contacts = dbHelper.getAllContacts();
        contactAdapter.setContacts(contacts);

        // Xử lý sự kiện khi nhấn nút "Thêm người liên hệ mới"
        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddContactDialog();
            }
        });
    }


    public void onContactsButtonClicked(View view) {

    }

    // Chuyển qua view quay số điện thoại
    public void onDialerButtonClicked(View view) {
        Intent intent = new Intent(this, DialerActivity.class);
        startActivity(intent);
    }

    // Hiển thị nhập liên hệ mới
    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);
        builder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextPhoneNumber = dialogView.findViewById(R.id.editTextPhoneNumber);
        Button buttonAdd = dialogView.findViewById(R.id.buttonAdd);

        final AlertDialog dialog = builder.create();
        dialog.show();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();

                // Kiểm tra xem liệu tên và số điện thoại có rỗng hay không
                if (!name.isEmpty() && !phoneNumber.isEmpty()) {
                    // Tạo một đối tượng Contact mới
                    Contact contact = new Contact(name, phoneNumber);

                    // Thêm dữ liệu vào cơ sở dữ liệu
                    DBHelper dbHelper = new DBHelper(getApplicationContext());
                    dbHelper.addContact(contact);

                    // Thêm dữ liệu vào RecyclerView
                    contactAdapter.addContact(contact);

                    // Hiển thị thông báo cho người dùng
                    Toast.makeText(getApplicationContext(), "Đã thêm: " + name + ", " + phoneNumber, Toast.LENGTH_SHORT).show();

                    // Đóng dialog
                    dialog.dismiss();
                } else {
                    // Nếu các trường không hợp lệ, bạn có thể hiển thị thông báo hoặc xử lý khác tùy thuộc vào yêu cầu của ứng dụng
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}