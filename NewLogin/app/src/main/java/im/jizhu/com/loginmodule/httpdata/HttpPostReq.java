package im.jizhu.com.loginmodule.httpdata;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import im.jizhu.com.loginmodule.DB.entity.UserEntity;
import im.jizhu.com.loginmodule.DB.sp.TimeSaveSp;
import im.jizhu.com.loginmodule.config.UrlConstant;
import im.jizhu.com.loginmodule.imservice.event.GroupEvent;
import im.jizhu.com.loginmodule.imservice.event.UserEntityEvent;
import im.jizhu.com.loginmodule.imservice.manager.IMLoginManager;
import im.jizhu.com.loginmodule.utils.Logger;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by it026 on 2017/6/26.
 */
public class HttpPostReq {
//
//    /**
//     * http请求登录相关数据
//     * @param context
//     */
//    public static void reqAllDataList(Context context) {
//
//        AsyncHttpClient client = new AsyncHttpClient();
//        String url = UrlConstant.accessHttpAddressReserve() ;//user,dept  + "user_id="+"2767"+"&time=0&type=all"
//        final int loginId = IMLoginManager.instance().getLoginId();
//        long time= System.currentTimeMillis()/1000;
//        String token = loginId+"|"+time;
//        /** 加密*/
//        String tokenString = new String(com.lesso.cc.Security.getInstance().EncryptMsg(token));
//        RequestParams params = new RequestParams();
//        TimeSaveSp.instance().init(context);
//        int roleTime = TimeSaveSp.instance().getRoleTimeValue("roletime" + loginId) ;
//        int userTime = TimeSaveSp.instance().getRoleTimeValue("usertime"+loginId) ;
//        int deptTime = TimeSaveSp.instance().getRoleTimeValue("depttime"+loginId) ;
//        int groupTime = TimeSaveSp.instance().getRoleTimeValue("grouptime"+loginId) ;
//        int sessionTime = TimeSaveSp.instance().getRoleTimeValue("sessiontime"+loginId) ;
//
//        params.put("token",tokenString);
//        params.put("dev","Android");
//        params.put("role_time",roleTime);
//        params.put("user_time",userTime);
//        params.put("dept_time",deptTime);
//        params.put("group_time",groupTime);
//        params.put("session_time", sessionTime);
//        //LogUtils.e("test", "登录访问信息：" + params);
//        client.setTimeout(30000);
//        client.post(url, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                //statusCode:状态码    headers：头信息  responseBody：返回的内容，返回的实体
//                //判断状态码
//                if (statusCode == 200) {
//                    //获取结果
//                    try {
//                        String result = new String(responseBody, "utf-8");
//                        //LogUtils.e("test", "result:" + result);
//                        JSONObject jsonObject = new JSONObject(result);
//                        String code = jsonObject.getString("Code");
//                        if (code.equals("0")) {
//                            JSONObject jsonObject2 = jsonObject.getJSONObject("Data");
//                            String flag = jsonObject2.getString("flag");
//                            JSONObject timeobject = jsonObject2.getJSONObject("time");
//                            String roleTime = timeobject.getString("role_time");
//                            String userTime = timeobject.getString("user_time");
//                            String deptTime = timeobject.getString("dept_time");
//                            String groupTime = timeobject.getString("group_time");
//                            String sessionTime = timeobject.getString("session_time");
//
//                            //保存登录返回时间值
//                            TimeSaveSp.instance().setFlagTimeValue("flagstatues"+loginId,Integer.parseInt(flag));
//                            TimeSaveSp.instance().setRoleTimeValue("roletime" + loginId, Integer.parseInt(roleTime));
//                            TimeSaveSp.instance().setUserTimeValue("usertime" + loginId, Integer.parseInt(userTime));
//                            TimeSaveSp.instance().setDeptTimeValue("depttime" + loginId, Integer.parseInt(deptTime));
//                            TimeSaveSp.instance().setGroupTimeValue("grouptime" + loginId, Integer.parseInt(groupTime));
//                            TimeSaveSp.instance().setDeptTimeValue("sessiontime" + loginId, Integer.parseInt(sessionTime));
//
//                            JSONArray jsonArrayuser = jsonObject2.getJSONArray("user");
//                            JSONArray jsonArrayDept = jsonObject2.getJSONArray("dept");
//                            JSONArray jsonArraysession = jsonObject2.getJSONArray("session");
//                            JSONArray jsonArraygroup = jsonObject2.getJSONArray("group");
//
//                            //LogUtils.e("test","jsonArrayuser:"+jsonArrayuser);
//                            //人员，部门数据下发
//                            if (jsonArrayuser.length()>0) {
//                                IMContactManager.instance().reqAllUserData(jsonArrayuser,jsonArrayDept,Integer.parseInt(flag));
//                            }
//                            if (jsonArrayDept.length()>0) {
//                                IMContactManager.instance().reqAllDepartment(jsonArrayDept, true);
//                            }
//                            //最近会话消息
//                            if (jsonArraysession.length()>0) {
//
//                                IMSessionManager.instance().reqGetRecentContacts(jsonArraysession);
//
//                            }else {
//                                IMSessionManager.instance().onLocalNetOk();//通过Socket再次请求最近会话
//                            }
//                            //群组数据下发
//                            if (jsonArraygroup.length()>0) {
//
//                                IMGroupManager.instance().reqAllNormalGroupList(jsonArraygroup);
//                            }
//                            Logger.e("HttpPostReq###基础数据获取,包括增量", "登录人员数据jsonArrayuser:"+jsonArrayuser);
//                            Logger.e("HttpPostReq###基础数据获取,包括增量", "最近消息会话数据jsonArraysession:" + jsonArraysession);
//
//
//                        }else {
//                            Logger.e("test", "数据异常:");
//                            JSONObject jsonErr = new JSONObject(result);
//                            String error = jsonErr.getString("Error");
//                            UserEntityEvent userEntityEvent = new UserEntityEvent();
//                            userEntityEvent.setEvent(UserEntityEvent.Event.HTTP_REQ_ERR);
//                            userEntityEvent.setErrReturn(error);
//                            triggerEvent(userEntityEvent);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
//                Logger.e("test", "数据出错:" + statusCode);
//                try {
//                    /*String result = new String(responseBody, "utf-8");
//                    Logger.e("test", "result:" + result);
//                    JSONObject jsonObject = new JSONObject(result);
//                    String error = jsonObject.getString("Error");*/
//                    UserEntityEvent userEntityEvent = new UserEntityEvent();
//                    userEntityEvent.setEvent(UserEntityEvent.Event.HTTP_REQ_ERR);
//                    userEntityEvent.setErrReturn("HTTP服务器访问失败,请检查后重新登录!");
//                    triggerEvent(userEntityEvent);
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//    }
//
//    /**
//     * 请求部门 变更 数据
//     * @param context
//     */
//    public static void reqDepartmentData(Context context) {
//
//        AsyncHttpClient client = new AsyncHttpClient();
//        String url = UrlConstant.accessHttpAddressReserve() ;//user,dept  + "user_id="+"2767"+"&time=0&type=all"
//        //long time= System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳 String  str=String.valueOf(time);
//        RequestParams params = new RequestParams();
//        TimeSaveSp.instance().init(context);
//        final int loginId = IMLoginManager.instance().getLoginId();
//        long time= System.currentTimeMillis()/1000;
//        String token = loginId+"|"+time;
//        /** 加密 */
//        String tokenString = new String(com.lesso.cc.Security.getInstance().EncryptMsg(token));
//
//        int roleTime = TimeSaveSp.instance().getRoleTimeValue("roletime" + loginId) ;
//        int deptTime = TimeSaveSp.instance().getRoleTimeValue("depttime"+loginId) ;
//
//        params.put("token",tokenString);
//        params.put("dev","Android");
//        params.put("role_time",roleTime);
//        params.put("user_time",-1);
//        params.put("dept_time",deptTime);
//        params.put("group_time",-1);
//        params.put("session_time",-1);
//
//        client.setTimeout(30000);
//        client.post(url, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                //statusCode:状态码    headers：头信息  responseBody：返回的内容，返回的实体
//                //判断状态码
//                if (statusCode == 200) {
//                    //获取结果
//                    try {
//                        String result = new String(responseBody, "utf-8");
//                        LogUtils.e("test", "result:" + result);
//                        JSONObject jsonObject = new JSONObject(result);
//                        String code = jsonObject.getString("Code");
//                        if (code.equals("0")) {
//                            JSONObject jsonObject2 = jsonObject.getJSONObject("Data");
//                            String flag = jsonObject2.getString("flag");
//                            JSONObject timeobject = jsonObject2.getJSONObject("time");
//                            String roleTime = timeobject.getString("role_time");
//                            //String userTime = timeobject.getString("user_time");
//                            String deptTime = timeobject.getString("dept_time");
//                            //String groupTime = timeobject.getString("group_time");
//                            String sessionTime = timeobject.getString("session_time");
//                            //保存登录返回时间值
//                            TimeSaveSp.instance().setFlagTimeValue("flagstatues" + loginId, Integer.parseInt(flag));
//                            TimeSaveSp.instance().setRoleTimeValue("roletime" + loginId, Integer.parseInt(roleTime));
//                            //TimeSaveSp.instance().setUserTimeValue("usertime" + loginId, Integer.parseInt(userTime));
//                            TimeSaveSp.instance().setDeptTimeValue("depttime" + loginId, Integer.parseInt(deptTime));
//                            //TimeSaveSp.instance().setGroupTimeValue("grouptime" + loginId, Integer.parseInt(groupTime));
//                            //TimeSaveSp.instance().setDeptTimeValue("sessiontime"+loginId,Integer.parseInt(sessionTime));
//                            //LogUtils.e("test", "flag:" + flag);
//                            //JSONArray jsonArrayuser = jsonObject2.getJSONArray("user");
//                            JSONArray jsonArrayDept = jsonObject2.getJSONArray("dept");
//                            //JSONArray jsonArraysession = jsonObject2.getJSONArray("session");
//                            //JSONArray jsonArraygroup = jsonObject2.getJSONArray("group");
//                            if (jsonArrayDept.length()>0) {
//                                IMContactManager.instance().reqAllDepartment(jsonArrayDept,false);
//                            }
//                        }else {
//
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
//
//            }
//        });
//
//    }
//
//
//    /**
//     * 搜索人员信息
//     * @param value
//     */
//    public static void reqUserSearchData1(String value) {
////        final List<UserEntity> userEntities = new ArrayList<>();
//        AsyncHttpClient client = new AsyncHttpClient();
//        String url = UrlConstant.accessHttpSearchUserAddressReserve() ;
//        final int loginId = IMLoginManager.instance().getLoginId();
//        long time= System.currentTimeMillis()/1000;
//        String token = loginId+"|"+time;
//        /** 加密 */
//        String tokenString = new String(com.lesso.cc.Security.getInstance().EncryptMsg(token));
//        RequestParams params = new RequestParams();
//        params.put("token",tokenString);
//        params.put("key", value);
//        params.put("dev","Android");
//        //LogUtils.e("test", "搜索值：" + tokenString + ";;时间：" + time);
//        client.setTimeout(30000);
//        client.post(url, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                if (i == 200) {
//                    try {
//                        String result = new String(bytes, "utf-8");
//                        Logger.e("test", "result:" + result);
//                        JSONObject jsonObject = new JSONObject(result);
//                        String code = jsonObject.getString("Code");
//                        if (code.equals("0")) {
//                            JSONArray jsonArraySearch = jsonObject.getJSONArray("Data");
//                            if (jsonArraySearch.length() > 0) {
//                                List<UserEntity> userEntities = new ArrayList<>();
//                                for (int z = 0; z < jsonArraySearch.length(); z++) {
//                                    UserEntity user = HttpJavaBean.getUserEntity(jsonArraySearch,z);
//                                    userEntities.add(user);
//                                }
//                                GroupEvent userEntityEven = new GroupEvent(GroupEvent.Event.USER_DATA_LIST);
//                                userEntityEven.setUserEntityList(userEntities);
//                                triggerEvent(userEntityEven);//聊天界面修改UI
//                            } else {
//                                //无数据
//                                GroupEvent userEntityEven = new GroupEvent(GroupEvent.Event.USER_DATA_NULL);
//                                triggerEvent(userEntityEven);//聊天界面修改UI
//                            }
//                        }else {
//                            GroupEvent userEntityEven = new GroupEvent(GroupEvent.Event.USER_DATA_NULL);
//                            triggerEvent(userEntityEven);//聊天界面修改UI
//                            UserEntityEvent userEntityEven1 = new UserEntityEvent();
////                            userEntityEven.setEvent(UserEntityEvent.Event.USER_DATA_NULL);
////                            triggerEvent(userEntityEven);//聊天界面修改UI
//
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//
//            }
//        });
////        return userEntities;
//    }
//
//
//    /**
//     * 搜索人员信息
//     * @param value
//     */
//    public static void reqUserSearchData(String value) {
//
//        AsyncHttpClient client = new AsyncHttpClient();
//        String url = UrlConstant.accessHttpSearchUserAddressReserve() ;
//        final int loginId = IMLoginManager.instance().getLoginId();
//        long time= System.currentTimeMillis()/1000;
//        String token = loginId+"|"+time;
//        /** 加密 */
//        String tokenString = new String(com.lesso.cc.Security.getInstance().EncryptMsg(token));
//        RequestParams params = new RequestParams();
//        params.put("token",tokenString);
//        params.put("key", value);
//        params.put("dev","Android");
//        //LogUtils.e("test", "搜索值：" + tokenString + ";;时间：" + time);
//        client.setTimeout(30000);
//        client.post(url, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                if (i == 200) {
//                    try {
//                        String result = new String(bytes, "utf-8");
//                        Logger.e("test", "result:" + result);
//                        JSONObject jsonObject = new JSONObject(result);
//                        String code = jsonObject.getString("Code");
//                        if (code.equals("0")) {
//                            JSONArray jsonArraySearch = jsonObject.getJSONArray("Data");
//                            if (jsonArraySearch.length() > 0) {
//                                List<UserEntity> userEntities = new ArrayList<>();
//                                for (int z = 0; z < jsonArraySearch.length(); z++) {
//                                    UserEntity user = HttpJavaBean.getUserEntity(jsonArraySearch,z);
//                                    userEntities.add(user);
//                                }
//                                UserEntityEvent userEntityEven = new UserEntityEvent();
//                                userEntityEven.setEvent(UserEntityEvent.Event.USER_DATA_LIST);
//                                userEntityEven.setUserEntityList(userEntities);
//                                triggerEvent(userEntityEven);//聊天界面修改UI
//                            } else {
//                                //无数据
//                                UserEntityEvent userEntityEvent = new UserEntityEvent();
//                                userEntityEvent.setEvent(UserEntityEvent.Event.USER_DATA_NULL);
//                                triggerEvent(userEntityEvent);//聊天界面修改UI
//                            }
//                        }else {
//
//                            UserEntityEvent userEntityEvent = new UserEntityEvent();
//                            userEntityEvent.setEvent(UserEntityEvent.Event.USER_DATA_NULL);
//                            triggerEvent(userEntityEvent);//聊天界面修改UI
//
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//
//            }
//        });
//    }
//
//    /**
//     * 以人员ID搜索
//     * @param peerId
//     */
//    public static void reqUserByUserIdSearchData(int peerId) {
//        AsyncHttpClient client = new AsyncHttpClient();
//        String url = UrlConstant.accessHttpSearchUserIdAddressReserve();
//        RequestParams params = new RequestParams();
//        final int loginId = IMLoginManager.instance().getLoginId();
//        long time= System.currentTimeMillis()/1000;
//        String token = loginId+"|"+time;
//        /** 加密 */
//        String tokenString = new String(com.lesso.cc.Security.getInstance().EncryptMsg(token));
//        params.put("token",tokenString);
//        params.put("key", peerId);
//        params.put("dev","Android");
//        //LogUtils.e("test", "搜索值：" + params);
//        client.setTimeout(30000);
//        client.post(url, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                if (i == 200) {
//                    try {
//                        String result = new String(bytes, "utf-8");
//                        Logger.e("test", "####人员数据请求成功，以人员ID搜索:");
//                        JSONObject jsonObject = new JSONObject(result);
//                        String code = jsonObject.getString("Code");
//                        if (code.equals("0")) {
//                            JSONArray jsonArraySearch = jsonObject.getJSONArray("Data");
//                            if (jsonArraySearch.length() > 0) {
//                                ArrayList<UserEntity> userEntities = new ArrayList<>();
//                                UserEntity user = new UserEntity();
//                                for (int z = 0; z < jsonArraySearch.length(); z++) {
//                                    user = HttpJavaBean.getUserEntity(jsonArraySearch,z);
//                                    userEntities.add(user);
//                                }
//                                if (userEntities.size()>0) {
//                                    IMContactManager.instance().inserSessionUserData(userEntities,user);
//                                }
//                            } else {
//                                //无数据
//                                Logger.e("test", "无数据");
//                            }
//                        }else {
//
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//            }
//        });
//    }
//
//
//    /**
//     * 自身的事件驱动 (搜索UI修改)
//     *
//     * @param event
//     */
//    public static void triggerEvent(Object event) {
//        EventBus.getDefault().post(event);
//    }

}
