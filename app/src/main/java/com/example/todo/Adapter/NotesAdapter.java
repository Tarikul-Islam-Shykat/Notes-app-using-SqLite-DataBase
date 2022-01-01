package com.example.todo.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.DataBase.DatabaseHelper;
import com.example.todo.Model.NoteModel;
import com.example.todo.R;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter< NotesAdapter.viewHolder> {

    Context context;
    Activity activity;
     ArrayList<NoteModel> arrayList;
     DatabaseHelper databaseHelper;

    public NotesAdapter(Context context, Activity activity, ArrayList<NoteModel> arrayList) {
        this.context = context;
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public NotesAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_list,parent, false );
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.viewHolder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.description.setText(arrayList.get(position).getDes());
        databaseHelper = new DatabaseHelper(context);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.delete(arrayList.get(position).getID());
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView delete, edit;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notes_title);
            description = itemView.findViewById(R.id.notes_description);
            delete = itemView.findViewById(R.id.notes_delete);
            edit = itemView.findViewById(R.id.notes_edit);
        }
    }

    public  void showDialog(int pos)
    {
        EditText title, des;
        Button submit;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);

        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        title = (EditText) dialog.findViewById(R.id.title);
        des = (EditText) dialog.findViewById(R.id.description);
        submit = (Button) dialog.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().isEmpty())
                {
                    title.setError("Please enter a title");
                }
                else if(des.getText().toString().isEmpty())
                {
                    des.setError("Please enter some description");
                }
                else
                {
                    databaseHelper.updateNotes(title.getText().toString(), des.getText().toString(), arrayList.get(pos).getID());
                    arrayList.get(pos).setTitle(title.getText().toString());
                    arrayList.get(pos).setDes(des.getText().toString());
                    dialog.cancel();
                    //notify list
                    notifyDataSetChanged();
                }
            }
        });



    }
}

