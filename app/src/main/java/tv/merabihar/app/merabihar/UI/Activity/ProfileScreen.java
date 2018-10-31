package tv.merabihar.app.merabihar.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import tv.merabihar.app.merabihar.Adapter.ListViewAdapter;
import tv.merabihar.app.merabihar.Model.BeanClass;
import tv.merabihar.app.merabihar.R;

public class ProfileScreen extends AppCompatActivity {

    private ListView listview;

    public int[] IMAGE = {R.drawable.table_tennis, R.drawable.water_sports,R.drawable.chana, R.drawable.patna};
    public String[] TITLE = {"Table Tennis in Patna", "Water Sports in Bihar","CHANA GHUGNI","PATNA"};
    public String[] DESCRIPTION = {"Table Tennis is slowly gaining reputation as a recreation in Patna metropolis. Both young women 7 boys are gaining interest on this recreation and playing at college, college & kingdom degrees. Patliputra Sports Complex in Patna has an indoor stadium with good enough facilities for  hosting Table Tennis tournaments. Patna town holds the benefit of hosting the seventy fifth Senior National Table Tennis Tournament at Pataliputra Sports Complex in 2012. ",
            "Bihar management has taken projects within the latest past to make the state extra adventure sports pleasant. The authorities consequently introduced its dream mission of “Adventure Water Sports Facility” at Ganga River to pave the manner for the introduction of various types of watersports in the famend river. ","Chana Ghughni is a zesty night snack thing from the food of Bihar. To a great degree normal yet similarly flavorful, this mouth-watering snack is set up in relatively every family unit of Bihar. Bubbled chickpeas, browned with onion and flavors alongside \"Chuda ka bhuja\" (flattened rice) makes it an ideal response to fulfill your craving. Flattened and dried gram is additionally used to make other salty snacks.This food is minimal spicy and can be eaten alone or with puri, paratha, chapati, murmura or fried poha.","Patna is the capital and greatest city of the region of Bihar in India. Patna is the second-greatest city in eastern India . Fluxed by the three hallowed waterways Ganga, Sone and the Poonpun, the voyage of Patna through the ages can exceptionally be a challenge of desire for wherever struggling with its life-chronicle. Having a place with the blue gathering of urban networks with a great heritage,it is the place the best sovereigns of India walked, and where the best blessed individuals addressed. "};

    private ArrayList<BeanClass> beanClassArrayList;
    private ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_profile_screen);
            listview = (ListView) findViewById(R.id.listview);
            beanClassArrayList = new ArrayList<BeanClass>();


            for (int i = 0; i < TITLE.length; i++) {

                BeanClass beanClass = new BeanClass(IMAGE[i], TITLE[i], DESCRIPTION[i]);
                beanClassArrayList.add(beanClass);

            }
            listViewAdapter = new ListViewAdapter(ProfileScreen.this, beanClassArrayList);
            listview.setAdapter(listViewAdapter);


        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
