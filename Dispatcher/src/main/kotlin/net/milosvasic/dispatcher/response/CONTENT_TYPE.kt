package net.milosvasic.dispatcher.response


enum class CONTENT_TYPE(val value: String) {

    CSS("text/css"),
    GIF("image/gif"),
    PNG("image/png"),
    HTML("text/html"),
    MPEG("audio/mpeg"),
    JPEG("image/jpeg"),
    PLAIN("text/plain"),
    PDF("application/pdf"),
    ZIP("application/zip"),
    XML("application/xml"),
    VORBIS("audio/vorbis"),
    JSON("application/json"),
    ICON("image/vnd.microsoft.icon"),
    MULTI_PART("multipart/form-data"),
    JAVASCRIPT("application/javascript"),
    X_FORM("application/x-www-form-urlencoded")


}