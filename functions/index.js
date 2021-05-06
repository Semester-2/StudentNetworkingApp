const functions = require("firebase-functions");

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();
exports.checkflag = functions.database.ref('/announcements/') //give your database path instead here
.onCreate((snapshot, context) => {
    const temptoken = 'dv_uvTu8S6CwIwIi_tajHJ:APA91bEDxwCxxgjZgBvoaE9Dp9VBe8cvRO7YkXlhf319bW_ToGpt7st8oEgpLnrnBoJbIIu2RLEPLfdpXtxKcVl2OLVLOl02BdP-qyvY4nr06M6zhgg_2DPJWxt8EEIWXLw1GGrcHw0a';
    // const flag = snapshot.before.val();   TO GET THE OLD VALUE BEFORE UPDATE
//    const flag = snapshot.after.val();
    let statusMessage = `Message from the clouds as test`
    console.log("Entered the method");
    var message = {
        notification: {
            title: 'cfunction',
            body: statusMessage
        },
        token: temptoken
    };
    admin.messaging().send(message).then((response) => {
        console.log("Message sent successfully:", response);
        return response;
    })
    .catch((error) => {
        console.log("Error sending message: ", error);
    });
});




