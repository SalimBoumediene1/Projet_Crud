package afip.cda.projet_crud;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import afip.cda.projet_crud.model.Book;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


class BooksAdaptater extends RecyclerView.Adapter<BooksAdaptater.ViewHolder> {
    private Context mContext;
    private List<Book> bookList;

    public BooksAdaptater() {

    }

    public BooksAdaptater(Context mContext, List<Book> bookList) {
        this.mContext = mContext;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BooksAdaptater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Creation de viewHolder qui va contenir la precsentation chaque element de la liste
        View item_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_book, parent, false);
        return new BooksAdaptater.ViewHolder(item_view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book returnedBook = bookList.get(position);
        holder.title.setText(returnedBook.getTitre());
        holder.author.setText(returnedBook.getAuteur());
        Picasso.with(mContext).load(returnedBook.get_thumbnail()).into(holder.image);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = returnedBook.getIsbn();
                Intent intent = new Intent(mContext,BookDetailActivity.class);
                intent.putExtra("id", id);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        ImageView image;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_book);
            author = itemView.findViewById(R.id.author_book);
            image = itemView.findViewById(R.id.image_book);
            card = itemView.findViewById(R.id.card_book);
        }
    }
}