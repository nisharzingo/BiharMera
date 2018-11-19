package tv.merabihar.app.merabihar.UI.Activity.FriendList;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.merabihar.app.merabihar.Adapter.ReferalPeopleListAdapter;
import tv.merabihar.app.merabihar.Model.UserProfile;
import tv.merabihar.app.merabihar.R;
import tv.merabihar.app.merabihar.Util.PreferenceHandler;
import tv.merabihar.app.merabihar.Util.ThreadExecuter;
import tv.merabihar.app.merabihar.Util.Util;
import tv.merabihar.app.merabihar.WebAPI.ProfileFollowAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndirectFriendFragment  extends Fragment {

    LinearLayout indirectViewContainer;
    RecyclerView recyclerView;
    private ProgressBar mProgressBar;
    String referalCode = "";

    // variables for invitation screen
    View inviteFriendsView;
    TextView mReferalCode;
    LinearLayout mWhatsapp,mFaceBook,mSms,mMore;
    String shareContent;

    public IndirectFriendFragment() {
        // Required empty public constructor
    }

    public static IndirectFriendFragment newInstance() {
        IndirectFriendFragment fragment = new IndirectFriendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try{
            View view = inflater.inflate(R.layout.fragment_indirect_friend, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.influnecer_indirect_friend_list);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_indirect);
            inviteFriendsView = view.findViewById(R.id.invite_friends_screen_indirect_view);
            indirectViewContainer = view.findViewById(R.id.indirect_list_container);

            mProgressBar.setVisibility(View.GONE);

            referalCode = PreferenceHandler.getInstance(getActivity()).getReferalcode();

            if(referalCode!=null&&!referalCode.isEmpty()){
                getDirectRefer(referalCode);
            }

            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private void getDirectRefer(final String code){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getDirectReferedProfile(code);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {
                                //Collections.shuffle(responseProfile);
                                System.out.println("Direct profile "+responseProfile.size());

                                getDirectParentRefer(code,responseProfile);
                            }
                            else
                            {
                                getDirectParentRefer(code,null);
                            }
                        }
                        else
                        {
                            getDirectParentRefer(code,null);
                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);
                        getDirectParentRefer(code,null);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getDirectParentRefer(final String code,final ArrayList<UserProfile> profilesList){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getInDirectReferedParentProfile(code);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {
                                //Collections.shuffle(responseProfile);

                                System.out.println("Parent profile "+responseProfile.size());

                               /* if(profilesList!=null&&profilesList.size()!=0){


                                    responseProfile.removeAll(profilesList);

                                }*/

                                System.out.println("Parent profile "+responseProfile.size());

                                getInDirectRefer(code,responseProfile,profilesList);
                            }
                            else
                            {

                                getInDirectRefer(code,null,profilesList);
                            }
                        }
                        else
                        {
                            getInDirectRefer(code,null,profilesList);
                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);
                        getInDirectRefer(code,null,profilesList);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getInDirectRefer(final String code,final ArrayList<UserProfile> profileArrayList,final ArrayList<UserProfile> profileArrayLists){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getInDirectReferedProfile(code);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {
                                //Collections.shuffle(responseProfile);
                                System.out.println("Super Parent profile "+responseProfile.size());

                                if(profileArrayList!=null&&profileArrayList.size()!=0){

                                    if(profileArrayLists!=null&&profileArrayLists.size()!=0){


                                        responseProfile.removeAll(profileArrayLists);

                                    }else {
                                        responseProfile.removeAll(profileArrayList);
                                    }



                            }else if(profileArrayLists!=null&&profileArrayLists.size()!=0){


                                    responseProfile.removeAll(profileArrayLists);

                                }

                                System.out.println("Super Parent profile "+responseProfile.size());

                                if(responseProfile!=null&&responseProfile.size()!=0){

                                    ReferalPeopleListAdapter adapter = new ReferalPeopleListAdapter(getActivity(),responseProfile);
                                    recyclerView.setAdapter(adapter);
                                    indirectViewContainer.setVisibility(View.VISIBLE);

                                }else{

                                    if(profileArrayList!=null){
                                        ReferalPeopleListAdapter adapter = new ReferalPeopleListAdapter(getActivity(),profileArrayList);
                                        recyclerView.setAdapter(adapter);
                                        indirectViewContainer.setVisibility(View.VISIBLE);
                                    }/*else if(profileArrayLists!=null){
                                        ReferalPeopleListAdapter adapter = new ReferalPeopleListAdapter(getActivity(),profileArrayLists);
                                        recyclerView.setAdapter(adapter);
                                        indirectViewContainer.setVisibility(View.VISIBLE);
                                    }*/else{
                                        showShareActivity();
                                    }



                                }



                            }
                            else
                            {
                                if(profileArrayList!=null){
                                    ReferalPeopleListAdapter adapter = new ReferalPeopleListAdapter(getActivity(),profileArrayList);
                                    recyclerView.setAdapter(adapter);
                                    indirectViewContainer.setVisibility(View.VISIBLE);
                                }/*else if(profileArrayLists!=null){
                                        ReferalPeopleListAdapter adapter = new ReferalPeopleListAdapter(getActivity(),profileArrayLists);
                                        recyclerView.setAdapter(adapter);
                                        indirectViewContainer.setVisibility(View.VISIBLE);
                                    }*/else{
                                    showShareActivity();
                                }


                            }
                        }
                        else
                        {
                            showShareActivity();

                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);

                        if(profileArrayList!=null){
                            ReferalPeopleListAdapter adapter = new ReferalPeopleListAdapter(getActivity(),profileArrayList);
                            recyclerView.setAdapter(adapter);
                            indirectViewContainer.setVisibility(View.VISIBLE);
                        }/*else if(profileArrayLists!=null){
                                        ReferalPeopleListAdapter adapter = new ReferalPeopleListAdapter(getActivity(),profileArrayLists);
                                        recyclerView.setAdapter(adapter);
                                        indirectViewContainer.setVisibility(View.VISIBLE);
                                    }*/else{
                            showShareActivity();
                        }

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }



    // hide friends view and show share activity
    private void showShareActivity() {

        inviteFriendsView.setVisibility(View.VISIBLE);

        shareContent = "Hi friends I get 50 coins from Mera Bihar, Install Mera Bihar and use my referral code and get 50 coins immediately.\n\n Use my referal code for Sign-Up MBR"+PreferenceHandler.getInstance(getActivity()).getUserId()+"\n http://bit.ly/2JXcOnw";
        mReferalCode = (TextView)inviteFriendsView.findViewById(R.id.referal_code_text_2);
        mWhatsapp = (LinearLayout)inviteFriendsView.findViewById(R.id.whatsapp_invite);
        mFaceBook = (LinearLayout)inviteFriendsView.findViewById(R.id.facebook_invite);
        mSms = (LinearLayout)inviteFriendsView.findViewById(R.id.sms_invite);
        mMore = (LinearLayout)inviteFriendsView.findViewById(R.id.more_invite);


        int profileId = PreferenceHandler.getInstance(getActivity()).getUserId();

        if(profileId!=0){
            String ref = "MBR"+profileId;
            String referCodeText = ref;
            //   String referCodeText = Base64.encodeToString(ref.getBytes(), Base64.DEFAULT);
            mReferalCode.setText(""+referCodeText);
        }

        mReferalCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = mReferalCode.getText().toString();

                if(text!=null&&!text.isEmpty()){

                    ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", text);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getActivity(), "Text copied", Toast.LENGTH_SHORT).show();

                }


            }
        });

        mWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                try {
                    Objects.requireNonNull(getActivity()).startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
                }

            }
        });

        mFaceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textBody = shareContent;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,!TextUtils.isEmpty(textBody) ? textBody : "");



                boolean facebookAppFound = false;
                List<ResolveInfo> matches = getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana") ||
                            info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.lite")) {
                        intent.setPackage(info.activityInfo.packageName);
                        facebookAppFound = true;
                        break;
                    }
                }
                if (!facebookAppFound) {
//                        String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + "https://play.google.com/store/apps/details?id=app.zingo.bihartourismguide";
//                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }
                startActivity(intent);
            }
        });

        mSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");

                smsIntent.putExtra("sms_body", shareContent);
                if (smsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    Log.d("SMS", "Can't resolve app for ACTION_SENDTO Intent");
                    try {
                        Intent smsIntent2 = new Intent(android.content.Intent.ACTION_VIEW);
                        smsIntent2.putExtra("sms_body", shareContent);
                        smsIntent2.setData(Uri.parse("sms:"));
                        startActivity(smsIntent2);
                    } catch (android.content.ActivityNotFoundException anfe) {
                        Log.d("Error SMS" , "Error");
                    }
                }

            }
        });

        mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Send to"));
            }
        });
    }



}
