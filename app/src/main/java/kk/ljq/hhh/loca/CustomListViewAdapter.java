package kk.ljq.hhh.loca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class CustomListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DriverInfo> books;
    private static LayoutInflater inflater = null;


    public CustomListViewAdapter(Context context, ArrayList<DriverInfo> data){

        mContext = context;
        books = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (convertView == null){

            view = inflater.inflate(R.layout.list_row, null);

            TextView title = (TextView) view.findViewById(R.id.title);
            TextView author = (TextView) view.findViewById(R.id.author);
            TextView pages = (TextView) view.findViewById(R.id.pages);
            CircleImageView image = (CircleImageView) view.findViewById(R.id.list_image);

            DriverInfo mBook = new DriverInfo();

            mBook = books.get(position);



            title.setText(mBook.getD_name());
            author.setText(mBook.getD_loc_name());
            pages.setText(mBook.getD_Email());

            if(mBook.getD_photo()!=null){

            Picasso.with(mContext).load(mBook.getD_photo()).into(image);}
            else {
                image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_account_circle_black_36dp));


            }



        }


        return view;
    }
}
