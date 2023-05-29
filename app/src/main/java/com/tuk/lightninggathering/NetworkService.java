//package com.tuk.lightninggathering;
//
//package com.flashmob_team.usr.flashmob_project.Network;
//
//import com.flashmob_team.usr.flashmob_project.Flashmob.MeetGetResult;
//import com.flashmob_team.usr.flashmob_project.Flashmob.MeetPost;
//import com.flashmob_team.usr.flashmob_project.Flashmob.MeetPostResult;
//import com.flashmob_team.usr.flashmob_project.Main.MeetingResult;
//import com.flashmob_team.usr.flashmob_project.Login.LoginPost;
//import com.flashmob_team.usr.flashmob_project.Login.LoginResult;
//import com.flashmob_team.usr.flashmob_project.SignUp.CategoryResult;
//import com.flashmob_team.usr.flashmob_project.SignUp.LocationResult;
//import com.flashmob_team.usr.flashmob_project.SignUp.SignUpResult;
//
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.http.Body;
//import retrofit2.http.GET;
//import retrofit2.http.Multipart;
//import retrofit2.http.POST;
//import retrofit2.http.Part;
//import retrofit2.http.Path;
//
//public interface NetworkService {
//    @POST("/signin")
//    Call<LoginResult> getLoginInResult(@Body LoginPost loginPost);
//
//    // 메인 번개 정보 가져오기
//    @GET("/meeting/{category_id}")
//    Call<MeetingResult> getMeetingResult(@Path("category_id") int category_id);
//
//    // 추가한 번개 정보 가져오기
//    @GET("/meetingData/{meet_title}")
//    Call<MeetGetResult> getMeetingTitleResult(@Path("meet_title") String meet_title);
//
//    // 번개 추가
//    @POST("/meeting")
//    Call<MeetPostResult> getMeetPostResult(@Body MeetPost meetPost);
//
//    @GET("/location")
//    Call<LocationResult> getLocationResult();
//
//    @GET("/category")
//    Call<CategoryResult> getCategoryResult();
//
//    @Multipart
//    @POST("/signup")
//    Call<SignUpResult> getSignUpResult(@Part("user_email") RequestBody user_email,
//                                       @Part("user_pw") RequestBody user_pw,
//                                       @Part("user_name") RequestBody user_name,
//                                       @Part("user_age") RequestBody user_age,
//                                       @Part("user_sex") RequestBody user_sex,
//                                       @Part("user_phone") RequestBody user_phone,
//                                       @Part MultipartBody.Part file);
//}
