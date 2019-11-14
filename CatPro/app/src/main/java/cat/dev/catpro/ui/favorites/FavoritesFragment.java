package cat.dev.catpro.ui.favorites;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cat.dev.catpro.Cat;
import cat.dev.catpro.CatAdapter;
import cat.dev.catpro.FavoritesHelper;
import cat.dev.catpro.R;

public class FavoritesFragment extends Fragment {


    private RecyclerView recyclerViewFavorites;
    private LinearLayoutManager layoutManager;
    private CatAdapter catAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerViewFavorites = root.findViewById(R.id.recyclerView_favorites);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewFavorites.setLayoutManager(layoutManager);
        catAdapter = new CatAdapter(getActivity());
        recyclerViewFavorites.setAdapter(catAdapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        FavoritesHelper databaseHelper = new FavoritesHelper(getActivity(),"favorite",null,1);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("favorites", null, null, null, null, null, null);
        if (cursor!=null){
            List<Cat> cats = new ArrayList<>();
            while(cursor.moveToNext()){
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                Cat cat= new Cat();
                cat.setId(id);
                cat.setName(name);
                cats.add(cat);
            }
            catAdapter.setData(cats);
        }
    }
}