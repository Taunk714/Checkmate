//package com.teamred.checkmate.ui;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.teamred.checkmate.data.model.Note;
//
//import java.util.ArrayList;
//
//public class NoteListViewAdapter extends BaseAdapter {
//
//    private Note[] noteList;
//    String episodes[];             //Keeping it simple.  Using Parallel arrays is the introductory way to store the List data.
//    String  episodeDescriptions[];  //the "better" way is to encapsulate the list items into an object, then create an arraylist of objects.
//    //     int episodeImages[];         //this approach is fine for now.
//    ArrayList<Integer> episodeImages;  //Well, we can use one arrayList too...  Just mixing it up here, Arrays or Templated ArrayLists, you choose.
//
////    ArrayList<String> episodes;
////    ArrayList<String> episodeDescriptions;
//
//    Button btnRandom;
//    Context context;   //Creating a reference to our context object, so we only have to get it once.  Context enables access to application specific resources.
//    // Eg, spawning & receiving intents, locating the various managers.
//
//    //STEP 2: Override the Constructor, be sure to:
//    // grab the context, we will need it later, the callback gets it as a parm.
//    // load the strings and images into object references.
//    public NoteListViewAdapter(Context aContext, Note[] list) {
////initializing our data in the constructor.
//        context = aContext;  //saving the context we'll need it again.
//        episodes =aContext.getResources().getStringArray(R.array.episodes);  //retrieving list of episodes predefined in strings-array "episodes" in strings.xml
//        episodeDescriptions = aContext.getResources().getStringArray(R.array.episode_descriptions);
//
////This is how you would do it if you were using an ArrayList, leaving code here for reference, though we could use it instead of the above.
////        episodes = (ArrayList<String>) Arrays.asList(aContext.getResources().getStringArray(R.array.episodes));  //retrieving list of episodes predefined in strings-array "episodes" in strings.xml
////        episodeDescriptions = (ArrayList<String>) Arrays.asList(aContext.getResources().getStringArray(R.array.episode_descriptions));  //Also casting to a friendly ArrayList.
//
//
//        episodeImages = new ArrayList<Integer>();   //Could also use helper function "getDrawables(..)" below to auto-extract drawable resources, but keeping things as simple as possible.
//        episodeImages.add(R.drawable.st_spocks_brain);
//        episodeImages.add(R.drawable.st_arena__kirk_gorn);
//        episodeImages.add(R.drawable.st_this_side_of_paradise__spock_in_love);
//        episodeImages.add(R.drawable.st_mirror_mirror__evil_spock_and_good_kirk);
//        episodeImages.add(R.drawable.st_platos_stepchildren__kirk_spock);
//        episodeImages.add(R.drawable.st_the_naked_time__sulu_sword);
//        episodeImages.add(R.drawable.st_the_trouble_with_tribbles__kirk_tribbles);
//    }
//
//    //STEP 3: Override and implement getCount(..),
//// ListView uses this to determine how many rows to render.
//    @Override
//    public int getCount() {
////        return episodes.size(); //all of the arrays are same length, so return length of any... ick!  But ok for now. :)
//        return episodes.length;   //all of the arrays are same length, so return length of any... ick!  But ok for now. :)
//        //Q: How else could we have done this (better)? ________________
//    }
//
//    //STEP 4: Override getItem/getItemId, we aren't using these, but we must override anyway.
//    @Override
//    public Object getItem(int position) {
////        return episodes.get(position);  //In Case you want to use an ArrayList
//        return episodes[position];        //really should be returning entire set of row data, but it's up to us, and we aren't using this call.
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;  //Another call we aren't using, but have to do something since we had to implement (base is abstract).
//    }
//
//    //THIS IS WHERE THE ACTION HAPPENS.  getView(..) is how each row gets rendered.
////STEP 5: Easy as A-B-C
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        //position is the index of the row being rendered.
//        //convertView represents the Row (it may be null),
//        // parent is the layout that has the row Views.
//
////STEP 5a: Inflate the listview row based on the xml.
//        View row;  //this will refer to the row to be inflated or displayed if it's already been displayed. (listview_row.xml)
////        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        row = inflater.inflate(R.layout.listview_row, parent, false);  //
//
////// Let's optimize a bit by checking to see if we need to inflate, or if it's already been inflated...
//        if (convertView == null){  //indicates this is the first time we are creating this row.
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  //Inflater's are awesome, they convert xml to Java Objects!
//            row = inflater.inflate(R.layout.listview_row, parent, false);
//        }
//        else
//        {
//            row = convertView;
//        }
//
////STEP 5b: Now that we have a valid row instance, we need to get references to the views within that row and fill with the appropriate text and images.
//        ImageView imgEpisode = (ImageView) row.findViewById(R.id.imgEpisode);  //Q: Notice we prefixed findViewByID with row, why?  A: Row, is the container.
//        TextView tvEpisodeTitle = (TextView) row.findViewById(R.id.tvEpisodeTitle);
//        TextView tvEpisodeDescription = (TextView) row.findViewById(R.id.tvEpisodeDescription);
//
//        tvEpisodeTitle.setText(episodes[position]);
//        tvEpisodeDescription.setText(episodeDescriptions[position]);
//        imgEpisode.setImageResource(episodeImages.get(position).intValue());
//
//        btnRandom = (Button) row.findViewById(R.id.btnRandom);
//        final String randomMsg = ((Integer)position).toString() +": "+ episodeDescriptions[position];
//        btnRandom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, randomMsg, Toast.LENGTH_LONG).show();
//            }
//        });
//
////STEP 5c: That's it, the row has been inflated and filled with data, return it.
//        return row;  //once the row is fully constructed, return it.  Hey whatif we had buttons, can we target onClick Events within the rows, yep!
////return convertView;
//
//    }
//
//    @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;
//    }
//}
