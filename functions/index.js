const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.addTimeStamp = functions.firestore
   .document('chats/{chatId}/messages/{messageId}')
   .onCreate((snap, context) => {
     if (snap) {
       return snap.ref.update({
                   timestamp: admin.firestore.FieldValue.serverTimestamp()
               });
     }

     return "snap was null or empty";
   });


exports.sendChatNotifications = functions.firestore
   .document('chats/{chatId}/messages/{messageId}')
   .onCreate((snap, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = snap.exists ? snap.data() : null;

     if (document) {
       var message = {
         notification: {
           title: document.from + ' sent you a message',
           body: document.text
         },
         topic: context.params.chatId
       };

       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('Successfully sent message:', response);
           return response;
         })
         .catch((error) => {
           console.log('Error sending message:', error);
           return error;
         });
     }

     return "document was null or emtpy";
   });



exports.sendChatNotifications = functions.firestore
   .document('goal/{user}/{messageId}')
   .onCreate((snap, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = snap.exists ? snap.data() : null;

     if (document) {
       var message = {
         notification: {
           title: document.from + ' sent you a message',
           body: document.text
         },
         topic: context.params.chatId
       };

       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('Successfully sent message:', response);
           return response;
         })
         .catch((error) => {
           console.log('Error sending message:', error);
           return error;
         });
     }

     return "document was null or emtpy";
   });





const {google} = require('googleapis');

exports.reply = (req, res) => {
  var jwt = getJwt();
  var apiKey = getApiKey();
  var spreadsheetId = '<PASTE YOUR SPREADSHEET ID HERE>';
  var range = 'A1';
  var row = [new Date(), 'A Cloud Function was here'];
  appendSheetRow(jwt, apiKey, spreadsheetId, range, row);
  res.status(200).type('text/plain').end('OK');
};

function getJwt() {
  var credentials = require("./google-services.json");
  return new google.auth.JWT(
    credentials.client_email, null, credentials.private_key,
    ['https://www.googleapis.com/auth/fitness']
  );
}

function getApiKey() {
  var apiKeyFile = require("./api_key.json");
  return apiKeyFile.key;
}


exports.addMessage = functions.https.onCall((data, context) => {
    const text = data.text;

      // [END readMessageData]
      // [START messageHttpsErrors]
      // Checking attribute.
      if (!(typeof text === 'string') || text.length === 0) {
        // Throwing an HttpsError so that the client gets the error details.
        throw new functions.https.HttpsError('invalid-argument', 'The function must be called with ' +
            'one arguments "text" containing the message text to add.');
      }
      // Checking that the user is authenticated.
      if (!context.auth) {
        // Throwing an HttpsError so that the client gets the error details.
        throw new functions.https.HttpsError('failed-precondition', 'The function must be called ' +
            'while authenticated.');
      }
      // [END messageHttpsErrors]

      // [START authIntegration]
      // Authentication / user information is automatically added to the request.
      const uid = context.auth.uid;
      const name = context.auth.token.name || null;
      const picture = context.auth.token.picture || null;
      const email = context.auth.token.email || null;
      // [END authIntegration]

      // [START returnMessageAsync]
      // Saving the new message to the Realtime Database.
      const sanitizedMessage = sanitizer.sanitizeText(text); // Sanitize the message.
      return admin.database().ref('/messages').push({
        text: text,
        author: { uid, name, picture, email },
      }).then(() => {
        console.log('New Message written');
        // Returning the sanitized message to the client.
        return { text: sanitizedMessage };
      })
      // [END returnMessageAsync]
      .catch((error) => {
        // Re-throwing the error as an HttpsError so that the client gets the error details.
        throw new functions.https.HttpsError('unknown', error.message, error);
      });
});