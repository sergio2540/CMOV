package my.game.achmed.Adapters;

import java.util.ArrayList;
import java.util.List;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.State.Peer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SplitPeersAdapter extends ArrayAdapter<Peer> {

	private List<Peer> peersToInflate;

	public SplitPeersAdapter(Context context, int resource, List<Peer> peers) {
		super(context, resource, peers);
		this.peersToInflate = peers;
	}

	/* here we must override the constructor for ArrayAdapter
	 * the only variable we care about now is ArrayList<Item> objects,
	 * because it is the list of objects we want to display.
	 */

	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
	public View getView(int position, View convertView, ViewGroup parent){

		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.ab_split_check, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		Peer p = peersToInflate.get(position);

		final Typeface font = Typeface.createFromAsset(ABEngine.context.getAssets(),"fonts/Kraash Black.ttf");
		
		if (p != null) {
			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.
			TextView tv = (TextView) v.findViewById(R.id.peerSplit);
			
			// check to see if each individual textview is null.
			// if not, assign some text!
			if (tv != null){
				tv.setTypeface(font);
				tv.setTextColor(Color.WHITE);
				tv.setText(p.getPeerName());
			}
			
		}

		// the view must be returned to our activity
		return v;

	}
	
	



}
