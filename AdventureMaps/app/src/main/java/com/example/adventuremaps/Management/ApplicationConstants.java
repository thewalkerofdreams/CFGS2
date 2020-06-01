package com.example.adventuremaps.Management;

public class ApplicationConstants {
    //Permissions
    public final static int REQUEST_CODE_PERMISSIONS_MAIN_TABBET_ACTIVITY_WITH_MAP = 2;
    public final static int REQUEST_CODE_PERMISSIONS_READ_EXTERNAL_STORAGE = 3;
    public final static int REQUEST_CODE_START_ACTIVITY_FOR_RESULT_IMAGE_GALLERY = 4;
    public final static int REQUEST_CODE_UPLOAD_IMAGE_FROM_OWN_GALLERY = 5;
    public final static int REQUEST_CODE_PERMISSIONS_FINE_AND_COARSE_LOCATION = 6;
    public final static int REQUEST_CODE_EDIT_LOCALIZATION_POINT = 7;
    public final static int REQUEST_CODE_DETAILS_LOCALIZATION_POINT = 8;
    public final static int REQUEST_CODE_CREATE_LOCALIZATION_POINT = 9;

    //Icons Size
    public final static int MARKER_WITH_SIZE = 70;
    public final static int MARKER_HEIGHT_SIZE = 70;
    public final static int MARKER_WITH_SIZE_ERROR_ANDROID_VERSION = 50;
    public final static int MARKER_HEIGHT_SIZE_ERROR_ANDROID_VERSION = 50;

    //Default Coordenates
    public final static double SEVILLE_LATITUDE = 37.3828300;
    public final static double SEVILLE_LONGITUDE = -5.9731700;

    //Maps Settings
    public final static int DEFAULT_LEVEL_ZOOM = 13;
    public final static double MIN_ZOOM_LEVEL_OFFLINE_MAP = 2;

    public final static String DEFAULT_MARKER_ICON_OFFLINE_MAPS = "DEFAULT_ICON";
    public final static String MARKER_SELECTED_ICON_OFFLINE_MAPS = "MARKER_SELECTED";

    public final static int DEFAULT_RIGHT_MARGIN_MAP = 20;
    public final static int DEFAULT_RIGHT_PADDING_MAP_BUT_NO_ACTIONBARSIZE_FOUND = 750;
    public final static int DEFAULT_RIGHT_MARGIN_OFFLINE_MAP_BUT_NO_ACTIONBARSIZE_FOUND = 60;
    public final static int DEFAULT_TOP_MARGIN_OFFLINE_MAP = 8;

    //AutoRestart App
    public final static String AUTORESTART_APP_CRASH = "crash";

    //Intents
    public final static String INTENT_LOGIN_EMAIL = "LoginEmail";
    public final static String INTENT_ACTUAL_EMAIL = "ActualEmail";
    public final static String INTENT_ACTUAL_USER_EMAIL = "ActualEmailUser";
    public final static String INTENT_ACTUAL_LATITUDE = "ActualLatitude";
    public final static String INTENT_ACTUAL_LONGITUDE = "ActualLongitude";

    public final static String INTENT_ACTUAL_ROUTE_ID = "ActualIdRoute";
    public final static String INTENT_ACTUAL_ROUTE_NAME = "ActualRouteName";

    public final static String INTENT_ACTUAL_LOCALIZATION = "ActualLocalization";
    public final static String INTENT_ACTUAL_LOCALIZATION_POINT = "ActualLocalizationPoint";

    public final static String INTENT_IMAGES_TO_SAVE = "ImagesToSave";

    public final static String INTENT_IMAGES_TO_LOAD = "ImagesToLoad";
    public final static String INTENT_POSITION_IMAGE_SELECTED = "PositionImageSelected";

    public final static String PHOTO_PICKER_TYPE = "image/*";

    public final static String INTENT_NAME_UPDATED = "NameUpdated";
    public final static String INTENT_DESCRIPTION_UPDATED = "DescriptionUpdated";

    //Datas
    public final static String DATA_LOCALIZATION_TO_SAVE = "LocalizationToSave";
    public final static String DATA_LOCALIZATION_TYPES_TO_SAVE = "LocalizationTypesToSave";

    public final static String DATA_LOCALIZATION_TYPES = "LocalizationTypes";
    public final static String DATA_LOCALIZATIONS_ID_ACTUAL_USER = "LocationsIdActualUser";

    //SharedPreferences
    public final static String SP_USER_LOGGED = "UserLogged";
    public final static String USER_LOGGED = "Logged";
    public final static String USER_NO_LOGGED = "NoLogged";

    public final static String SP_ACTUAL_USER_EMAIL = "UserActualEmail";

    public final static String SP_ORDER_LOCALIZATION_LIST_BY_FIELD = "OrderLocalizationListField";
    public final static String SP_ORDER_LOCALIZATION_LIST_BY_FAV = "OrderLocalizationListFav";

    public final static String SP_ORDER_ROUTE_LIST_BY_FIELD = "OrderRouteListField";
    public final static String SP_ORDER_ROUTE_LIST_BY_FAV = "OrderRouteListFav";

    //Firebase Addresses
    public final static String FB_USERS_ADDRESS = "Users";
    public final static String FB_LOCALIZATIONS_ID = "localizationsId";
    public final static String FB_USER_EMAIL_CHILD = "email";
    public final static String FB_USER_NICKNAME_CHILD = "nickName";

    public final static String FB_ROUTES_ADDRESS = "routes";
    public final static String FB_ROUTE_FAVOURITE_CHILD = "favourite";
    public final static String FB_ROUTE_NAME_CHILD = "name";
    public final static String FB_ROUTE_POINTS_CHILD = "routePoints";

    public final static String FB_LOCALIZATIONS_ADDRESS = "Localizations";
    public final static String FB_LOCALIZATION_POINT_ID = "localizationPointId";
    public final static String FB_LOCATION_LATITUDE = "latitude";
    public final static String FB_LOCATION_LONGITUDE = "longitude";
    public final static String FB_LOCATION_SHARED_CHILD = "shared";
    public final static String FB_LOCATION_NAME_CHILD = "name";
    public final static String FB_LOCATION_DESCRIPTION_CHILD = "description";
    public final static String FB_LOCATION_EMAIL_CREATOR_CHILD = "emailCreator";
    public final static String FB_LOCATION_INDEX_CHILD = "index";

    public final static String FB_LOCALIZATION_VALORATIONS_CHILD = "valorations";
    public final static String FB_LOCALIZATION_VALORATION_CHILD = "Valoration";

    public final static String FB_EMAIL_IMAGES = "emailImages";
    public final static String FB_LOCALIZATION_IMAGES = "LocalizationImages";
    public final static String FB_IMAGES_URI_CHILD = "Uri";

    public final static String FB_LOCALIZATION_TYPES_CHILD = "types";

    public final static String FB_STORAGE_IMAGES = "Images";

    //Firebase Keys
    public final static int FB_MIN_PASSWORD_SIZE = 6;

    //MIUI
    public final static String MIUI_OP_AUTO_START = "miui.intent.action.OP_AUTO_START";
    public final static String MIUI_SECURITY_CENTER = "com.miui.securitycenter";
    public final static String MIUI_AUTO_START_MANAGEMENT = "com.miui.permcenter.autostart.AutoStartManagementActivity";
    public final static String MIUI_POWER_HIDE_MODE_LIST = "miui.intent.action.POWER_HIDE_MODE_APP_LIST";
    public final static String MIUI_POWER_SETTINGS = "com.miui.powercenter.PowerSettings";

    //JSON
    public final static String JSON_CHARSET = "UTF-8";
    public final static String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";

    //TAGS
    public final static String TAG_LOCATION_FAV_TYPE = "Fav";
    public final static String TAG_LOCATION_OWNER_TYPE = "Owner";
    public final static String TAG_LOCATION_NO_OWNER_TYPE = "NoOwner";
}
