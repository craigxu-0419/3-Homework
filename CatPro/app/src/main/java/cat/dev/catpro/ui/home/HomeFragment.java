package cat.dev.catpro.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import cat.dev.catpro.Cat;
import cat.dev.catpro.CatAdapter;
import cat.dev.catpro.R;
import cat.dev.catpro.Url;

public class HomeFragment extends Fragment {


    private RecyclerView resultRecyclerView;
    private Button goSearch;
    private EditText editTextName;
    private LinearLayoutManager layoutManager;
    private CatAdapter catAdapter;
    private ProgressDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        resultRecyclerView = root.findViewById(R.id.recyclerView_result);
        editTextName = root.findViewById(R.id.et_catname);
        goSearch = root.findViewById(R.id.go_search);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = new ProgressDialog(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        resultRecyclerView.setLayoutManager(layoutManager);
        catAdapter = new CatAdapter(getActivity());
        resultRecyclerView.setAdapter(catAdapter);
        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSearch();
            }
        });
    }

    private void goSearch() {
        String text = editTextName.getText().toString();
        if (TextUtils.isEmpty(text)){
            Toast.makeText(getActivity(),"Search cats here",Toast.LENGTH_SHORT).show();

        }else{
            requestByVolley(Url.searchByName+text);
        }
    }

    public void requestByVolley(String url){
        dialog.show();
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("The request failed.");
                dialog.dismiss();
            } };
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response = JSON string, so you can do parsing with Gson here
                //o ThusyouneedtoconverttheresponseintoanArrayorList<>usingGson.
                Gson gson = new Gson();
                Cat[] catArray = gson.fromJson(response, Cat[].class);
                List<Cat> cats = Arrays.asList(catArray);
                catAdapter.setData(cats);
                dialog.dismiss();

            } };

        StringRequest stringRequest =
                new StringRequest(Request.Method.GET, url, responseListener, errorListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}