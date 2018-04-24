package com.countonit.user.swipetabpractice.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.countonit.user.swipetabpractice.ExpenseImageInfo;
import com.countonit.user.swipetabpractice.R;
import com.countonit.user.swipetabpractice.SquareImageView;
import com.countonit.user.swipetabpractice.database.MainDB;

import java.util.ArrayList;

/**
 * Created by User on 11/20/2017.
 */

public class GaleryAdapter extends RecyclerView.Adapter<GaleryAdapter.MyHolderView> {


    private final Context context;
    ArrayList<String> expList;


    public GaleryAdapter(Context context,ArrayList<String> expList){
        this.expList=expList;
        this.context=context;

    }

    @Override
    public MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.layout_gridimageview,parent,false);

        return new MyHolderView(view);
    }

    @Override
    public void onBindViewHolder(final MyHolderView holder, final int position) {

        try {
            MainDB mainDB=new MainDB(context);
            byte[] img=mainDB.getExpenseImage(mainDB.getIdBy(expList.get(position)));
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length, new BitmapFactory.Options()));
            holder.title.setText(expList.get(position).substring(0,1).toUpperCase()+expList.get(position).substring(1));
        }catch (NullPointerException np){
            np.printStackTrace();
        }

       /* holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ExpenseImageInfo info=new ExpenseImageInfo();
                AlertDialog dialog=info.makeDialog(context,holder.title.getText().toString(),position);
                dialog.show();

            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return expList.size();
    }

    static class MyHolderView extends RecyclerView.ViewHolder{

        SquareImageView imageView;
        TextView title;


        public MyHolderView(View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.gridImageView);
            title= itemView.findViewById(R.id.title);

        }

    }

}
