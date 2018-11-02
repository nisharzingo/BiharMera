package tv.merabihar.app.merabihar.WebAPI;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by ZingoHotels Tech on 02-11-2018.
 */

public interface UploadApi {

    @Multipart
    //@POST("Upload/user/PostUserImage")
    @POST("Upload/user/PostActivityImage")
    Call<String> uploadActivityImage(@Part MultipartBody.Part file, @Part("UploadedImage") RequestBody name);

    @Multipart
    //@POST("Upload/user/PostUserImage")
    @POST("Upload/user/PostCategoryImage")
    Call<String> uploadCategoryImage(@Part MultipartBody.Part file, @Part("UploadedImage") RequestBody name);

    @Multipart
    //@POST("Upload/user/PostUserImage")
    @POST("Upload/user/PostSubCategoryImage")
    Call<String> uploadSubCategoryImage(@Part MultipartBody.Part file, @Part("UploadedImage") RequestBody name);

    @Multipart
    //@POST("Upload/user/PostUserImage")
    @POST("Upload/user/PostBlogsImage")
    Call<String> uploadBlogImages(@Part MultipartBody.Part file, @Part("UploadedImage") RequestBody name);

    @Multipart
    //@POST("Upload/user/PostUserImage")
    @POST("Upload/user/PostProfileImage")
    Call<String> uploadProfileImages(@Part MultipartBody.Part file, @Part("UploadedImage") RequestBody name);
}
