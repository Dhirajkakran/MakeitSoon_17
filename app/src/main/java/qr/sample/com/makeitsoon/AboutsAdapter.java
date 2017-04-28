package qr.sample.com.makeitsoon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by dhiraj.kumar on 4/13/2016.
 */
public class AboutsAdapter extends  RecyclerView.Adapter<AboutsAdapter.MyViewHolder> {

    private ArrayList<mobAboutMetaData> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, timeime, genre,adress,distance,deviceid;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.firstvalue);
            genre = (TextView) view.findViewById(R.id.lastvalue);
            timeime = (TextView) view.findViewById(R.id.timeime);
            adress = (TextView) view.findViewById(R.id.adree);
            distance= (TextView) view.findViewById(R.id.distance);
            deviceid= (TextView) view.findViewById(R.id.deviceid);
        }
    }


    public AboutsAdapter(ArrayList<mobAboutMetaData> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_about, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mobAboutMetaData movie = moviesList.get(position);
        holder.title.setText(""+movie.getLat());
        holder.genre.setText(""+movie.getLon());
       // String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        holder.timeime.setText(movie.getDate());
        holder.adress.setText(""+movie.getCity());
        holder.distance.setText(movie.getDis());
        holder.deviceid.setText(movie.getDeviceid());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
    /*private static ArrayList<mobAboutMetaData> listContact;

    private LayoutInflater mInflater;

    public AboutsAdapter(Context photosFragment, ArrayList<mobAboutMetaData> results){
        listContact = results;
        mInflater = LayoutInflater.from(photosFragment);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listContact.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return listContact.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.layout_list_about, null);
            holder = new ViewHolder();
           // holder.product_img = (ImageView) convertView.findViewById(R.id.prodouct_name_img);
            holder.firstvalue = (TextView) convertView.findViewById(R.id.firstvalue);
            holder.lastvalue = (TextView) convertView.findViewById(R.id.lastvalue);
           // holder.detail_id = (TextView) convertView.findViewById(R.id.detail_id);
           // holder.validupto_id = (TextView) convertView.findViewById(R.id.validupto_id);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

         holder.firstvalue.setText(listContact.get(position).getHeaderText());
         holder.lastvalue.setText(listContact.get(position).getTextMsg());
        //holder.detail_id.setText(listContact.get(position).getPurchse_date());
        //holder.validupto_id.setText(listContact.get(position).getWarrantyupto());
        return convertView;
    }*/

  /*  static class ViewHolder{
        TextView firstvalue,lastvalue,detail_id;
        ImageView product_img;
    }*/

//}

