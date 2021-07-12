package com.example.graphicuserinterface.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.graphicuserinterface.R;
import com.example.graphicuserinterface.objects.Socket;

import java.util.List;

public class SocketAdapter extends ArrayAdapter<Socket> {
    private Context context;
    private int resource;
    private List<Socket> socketList;
    private LayoutInflater layoutInflater;

    public SocketAdapter(@NonNull Context context, int resource, @NonNull List<Socket> socketList, LayoutInflater layoutInflater) {
        super(context, resource, socketList);
        this.context = context;
        this.resource = resource;
        this.socketList = socketList;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = layoutInflater.inflate(resource, parent, false);
        Socket socket = socketList.get(position);

        if(socket != null){
            TextView tv1 = view.findViewById(R.id.idPriza);
            tv1.setText(socket.getId());

            TextView tv2 = view.findViewById(R.id.stareSocket1);
            tv2.setText(Boolean.toString(socket.getStateIndex(0)));

            TextView tv3 = view.findViewById(R.id.stareSocket2);
            tv3.setText(Boolean.toString(socket.getStateIndex(1)));

            TextView tv4 = view.findViewById(R.id.stareSocket3);
            tv4.setText(Boolean.toString(socket.getStateIndex(2)));

            TextView tv5 = view.findViewById(R.id.stareSocket4);
            tv5.setText(Boolean.toString(socket.getStateIndex(3)));
        }

        return view;
    }
}
