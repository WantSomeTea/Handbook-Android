package com.sergey.handbook.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sergey.handbook.Employee;
import com.sergey.handbook.R;

import java.util.List;

/**
 * Created by Sergey.
 */
class ContactsAdapter extends ArrayAdapter<Employee> {
    private Context context;
    private List<Employee> objects;
    private int resource;

    ContactsAdapter(Context context, int resource, List<Employee> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);
        TextView nameCell = (TextView) view.findViewById(R.id.CONTACTS_NAME_CELL);
        TextView pbxCell = (TextView) view.findViewById(R.id.textView_PBX);
        Employee employee = objects.get(position);
        nameCell.setText(employee.getName());
        pbxCell.setText(employee.getWorkNumber());
        return view;
    }
}
