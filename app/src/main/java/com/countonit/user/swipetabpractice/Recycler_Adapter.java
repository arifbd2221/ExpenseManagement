package com.countonit.user.swipetabpractice;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.countonit.user.swipetabpractice.database.MainDB;
import com.github.pavlospt.roundedletterview.RoundedLetterView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by User on 7/20/2017.
 */

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.ViewHolder> {

    PayerList context;
    PayeeList context2;
    LayoutInflater inflater;
    ArrayList<PP_Info> pp_infosList;
    String phoneCall=null;
    int a = 0;

    public Recycler_Adapter(Context context, ArrayList<PP_Info> pp_infosList) {
        if (context.getClass().getSimpleName().equals("PayerList")) {
            this.context = (PayerList) context;
            a = 1;
        }
        if (context.getClass().getSimpleName().equals("PayeeList")) {
            a = 0;
            this.context2 = (PayeeList) context;
        }
        inflater = LayoutInflater.from(context);
        this.pp_infosList = pp_infosList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Recycler_Adapter.ViewHolder holder, final int position) {


        holder.roundedLetterView.setBackgroundColor(getRandomColor());
        holder.roundedLetterView.setTitleText(pp_infosList.get(position).getNames().substring(0, 1).toUpperCase());
        holder.name.setText(pp_infosList.get(position).getNames().toUpperCase());
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences("CurrencyPosition", Context.MODE_PRIVATE);
            holder.amn.setText(pp_infosList.get(position).getAmounts() + " " + context.getResources().getStringArray(R.array.symbols1)[sp.getInt("position", 1) - 1]);

            SpannableString content = new SpannableString("Add To Income");
            content.setSpan(new UnderlineSpan(), 0, "Add To Income".length(), 0);
            holder.addTo.setText(content);

        } else {
            SharedPreferences sp = context2.getSharedPreferences("CurrencyPosition", Context.MODE_PRIVATE);
            holder.amn.setText(pp_infosList.get(position).getAmounts() + " " + context2.getResources().getStringArray(R.array.symbols1)[sp.getInt("position", 1) - 1]);

            SpannableString content = new SpannableString("Add To Expense");
            content.setSpan(new UnderlineSpan(), 0, "Add To Expense".length(), 0);
            holder.addTo.setText(content);
        }
        phoneCall=pp_infosList.get(position).getPhone();
        holder.phone.setText(phoneCall+"");
        holder.emal.setText(pp_infosList.get(position).getEmails());
        holder.date.setText(pp_infosList.get(position).getDates());


        if (context != null) {
            if (!context.is_in_action_mode) {
                holder.checkBox.setVisibility(View.GONE);
            } else {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(false);
            }
        } else {
            if (!context2.is_in_action_mode) {
                holder.checkBox.setVisibility(View.GONE);
            } else {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(false);
            }
        }


        holder.addTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                holder.addTo.setTextColor(Color.BLUE);

                if (context != null) {
                    Toast.makeText(context, "Added To Income Balance", Toast.LENGTH_SHORT).show();
                    DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
                    DateFormat dateFormat2 = new SimpleDateFormat("HH:mm a");
                    Date d = new Date();
                    new MainDB(context).insertIncome(Integer.parseInt(pp_infosList.get(position).getAmounts().toString()), dateFormat1.format(d), Calendar.getInstance().get(Calendar.MONTH) + 1, holder.name.getText().toString(), ""
                            , dateFormat2.format(d)
                    );


                } else {
                    SharedPreferences sharedPreferences = context2.getSharedPreferences("PayeeAsExpense", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    Toast.makeText(context2, "Added To Expense", Toast.LENGTH_SHORT).show();
                    String[] s = holder.amn.getText().toString().split(" ");
                    new MainDB(context2).createCategoryExpense("Payee", holder.name.getText().toString(), Integer.parseInt(pp_infosList.get(position).getAmounts().toString()), Integer.parseInt(s[0]));

                    editor.putString(holder.name.getText().toString(), "Payee");
                    editor.commit();
                }
            }
        });


        holder.docall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (context != null) {
                    call(context);

                }else {
                    call(context2);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return pp_infosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,amn,emal,date,addTo,phone;
        CheckBox checkBox;
        CardView cardView;
        RoundedLetterView roundedLetterView;

        RelativeLayout docall;

        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.text_element);
            amn=(TextView) itemView.findViewById(R.id.text_element2);
            emal=(TextView) itemView.findViewById(R.id.email_id);
            date=(TextView) itemView.findViewById(R.id.date);
            checkBox=(CheckBox) itemView.findViewById(R.id.checkBox);
            phone=(TextView) itemView.findViewById(R.id.phone);
            addTo=(TextView) itemView.findViewById(R.id.addToIncomeExpense);
            cardView=(CardView) itemView.findViewById(R.id.ppitem_card_view);
            docall=itemView.findViewById(R.id.docall);
            roundedLetterView=(RoundedLetterView) itemView.findViewById(R.id.rlv_name_view);
            if (context != null){
                cardView.setOnLongClickListener(context);
            }

            else {
                cardView.setOnLongClickListener(context2);
            }
            checkBox.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (context !=null)
            context.prepareCheckBox(v,getAdapterPosition());
            else
                context2.prepareCheckBox(v,getAdapterPosition());

        }

    }

    public void updateAdapter(ArrayList<PP_Info> list){

        MainDB dataBase=null;

        if ( a ==0 )
        dataBase = new MainDB(context2);
        else
            dataBase = new MainDB(context);

        for (PP_Info i : list){
            dataBase.deleteRecords(i.getNames(),i.getEmails(),i.getDates(),a);
            pp_infosList.remove(i);
        }
        notifyDataSetChanged();
    }



    public void filterAdapter(ArrayList<PP_Info> newListOnfo){
        pp_infosList=new ArrayList<>();
        pp_infosList.addAll(newListOnfo);
        notifyDataSetChanged();
    }

    int getRandomColor(){

        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        int color= Color.rgb(r,g,b);

        return color;


    }


    private void call(Context context){
        Intent intent = null;
        if (phoneCall != null) {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneCall));
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            context.startActivity(intent);
        }else {
            Toast.makeText(context,"Sorry Couldn't Found Any Number",Toast.LENGTH_SHORT).show();
        }
    }

}
