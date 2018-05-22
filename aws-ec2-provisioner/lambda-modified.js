/* This code has been generated from your interaction model by skillinator.io
/* eslint-disable  func-names */
/* eslint quote-props: ["error", "consistent"]*/

// There are three sections, Text Strings, Skill Code, and Helper Function(s).
// You can copy and paste the contents as the code for a new Lambda function, using the alexa-skill-kit-sdk-factskill template.
// This code includes helper functions for compatibility with versions of the SDK prior to 1.0.9, which includes the dialog directives.


// Load the SDK for JavaScript
var AWS = require('aws-sdk');

// Set the region
AWS.config.update({region: 'us-west-2'});


 // 1. Text strings =====================================================================================================
 //    Modify these strings and messages to change the behavior of your Lambda function


let speechOutput;
let reprompt;
let welcomeOutput = "This is a placeholder welcome message. This skill includes 16 intents. Try one of your intent utterances to test the skill.";
let welcomeReprompt = "sample re-prompt text";
// 2. Skill Code =======================================================================================================
"use strict";
const Alexa = require('alexa-sdk');
const http = require('http');
const APP_ID = undefined;  // TODO replace with your app ID (OPTIONAL).
speechOutput = '';
const handlers = {
	'LaunchRequest': function () {
		this.emit(':ask', welcomeOutput, welcomeReprompt);
	},
	'AMAZON.HelpIntent': function () {
		speechOutput = 'Placeholder response for AMAZON.HelpIntent.';
		reprompt = '';
		this.emit(':ask', speechOutput, reprompt);
	},
   'AMAZON.CancelIntent': function () {
		speechOutput = 'Placeholder response for AMAZON.CancelIntent';
		this.emit(':tell', speechOutput);
	},
   'AMAZON.StopIntent': function () {
		speechOutput = 'Placeholder response for AMAZON.StopIntent.';
		this.emit(':tell', speechOutput);
   },
   'SessionEndedRequest': function () {
		speechOutput = '';
		//this.emit(':saveState', true);//uncomment to save attributes to db on session end
		this.emit(':tell', speechOutput);
   },
	'AMAZON.YesIntent': function () {
		speechOutput = '';

		//any intent slot variables are listed here for convenience


		//Your custom intent handling goes here
		speechOutput = "This is a place holder response for the intent named AMAZON.YesIntent. This intent has no slots. Anything else?";
		this.emit(":ask", speechOutput, speechOutput);
    },
	'AMAZON.NoIntent': function () {
		speechOutput = '';

		//any intent slot variables are listed here for convenience


		//Your custom intent handling goes here
		speechOutput = "This is a place holder response for the intent named AMAZON.NoIntent. This intent has no slots. Anything else?";
		this.emit(":ask", speechOutput, speechOutput);
    },
	'CreateVMInstancesIntent': function () {
		//delegate to Alexa to collect all the required slot values
       let filledSlots = delegateSlotCollection.call(this);
		speechOutput = '';
		//any intent slot variables are listed here for convenience

		let numberSlot = resolveCanonical(this.event.request.intent.slots.number);
		console.log(numberSlot);
		let imageSlot = resolveCanonical(this.event.request.intent.slots.image);
		console.log(imageSlot);

		let confirmSlot = this.event.request.intent.confirmationStatus;
		console.log(confirmSlot);

		if(confirmSlot != "DENIED"){
			//uncomment this
			//let path = '/provision/hosts/create?image_tag='+imageSlot+'&count='+numberSlot;
			let path = '/provision/hosts/query?host_tag='+imageSlot+'&param=cpu';
			httpGet(path,  myResult => {
            console.log('myResult');
            console.log(myResult);
            this.response.speak(myResult).listen('try again');
            this.emit(':responseReady');

		});
		} else {
			 speechOutput = "This request has been terminated";
			 this.emit(':ask', speechOutput, speechOutput);
		}


	},
	'ScaleUpServerGroupIntent': function () {
		//delegate to Alexa to collect all the required slot values
       let filledSlots = delegateSlotCollection.call(this);
		speechOutput = '';
		//any intent slot variables are listed here for convenience

		let numberSlot = resolveCanonical(this.event.request.intent.slots.number);
		console.log(numberSlot);
		let imageSlot = resolveCanonical(this.event.request.intent.slots.image);
		console.log(imageSlot);
		let confirmSlot = this.event.request.intent.confirmationStatus;
		console.log(confirmSlot);

		if(confirmSlot != "DENIED"){
			// uncomment this
			//let path = '/provision/hosts/create?image_tag='+imageSlot+'&count=1';
			let path = '/provision/hosts/query?host_tag='+imageSlot+'&param=cpu';
			httpGet(path,  myResult => {
            console.log('myResult');
            console.log(myResult);
            this.response.speak(myResult).listen('try again');
            this.emit(':responseReady');
			});
		} else {
			 speechOutput = "This request has been terminated";
			 this.emit(':ask', speechOutput, speechOutput);
		}

	},
	'ScaleDownServerGroupIntent': function () {
		//delegate to Alexa to collect all the required slot values
       let filledSlots = delegateSlotCollection.call(this);
		speechOutput = '';
		//any intent slot variables are listed here for convenience

		let numberSlot = resolveCanonical(this.event.request.intent.slots.number);
		console.log(numberSlot);
		let confirmSlot = this.event.request.intent.confirmationStatus;
		console.log(confirmSlot);

		if(confirmSlot != "DENIED"){
			let path = '/provision/hosts/state?host_tag=app&action=terminate';
			//let path = '/provision/hosts/query?host_tag='+numberSlot+'&param=cpu';
			httpGet(path,  myResult => {
            console.log('myResult');
            console.log(myResult);
            this.response.speak(myResult).listen('try again');
            this.emit(':responseReady');
			});
		} else {
			 speechOutput = "This request has been terminated";
			 this.emit(':ask', speechOutput, speechOutput);
		}


	},
	'ResizeServerGroupIntent': function () {
		//delegate to Alexa to collect all the required slot values
       let filledSlots = delegateSlotCollection.call(this);
		speechOutput = '';
		//any intent slot variables are listed here for convenience

		let numberSlotRaw = this.event.request.intent.slots.number.value;
		console.log(numberSlotRaw);
		let numberSlot = resolveCanonical(this.event.request.intent.slots.number);
		console.log(numberSlot);

		//Your custom intent handling goes here
		speechOutput = "Resized cluster to "+numberSlot+" instances";
		this.emit(':ask', speechOutput, speechOutput);
	},
	'UpdateVMInstanceTypeIntent': function () {
		speechOutput = '';

		//any intent slot variables are listed here for convenience

		let typeSlot = resolveCanonical(this.event.request.intent.slots.type);
		console.log(typeSlot);
		let confirmSlot = this.event.request.intent.confirmationStatus;
		console.log(confirmSlot);

		if(confirmSlot != "DENIED"){
			// Add correct path
			let path = '/provision/hosts/query?host_tag='+typeSlot+'&param=cpu';
			httpGet(path,  myResult => {
            console.log('myResult');
            console.log(myResult);
            this.response.speak(myResult).listen('try again');
            this.emit(':responseReady');
			});
		} else {
			 speechOutput = "This request has been terminated";
		}

		//Your custom intent handling goes here

		this.emit(":ask", speechOutput, speechOutput);
    },
	'FindPendingRequestIntent': function () {
		speechOutput = '';

		//any intent slot variables are listed here for convenience


		//Your custom intent handling goes here
		speechOutput = "This is a place holder response for the intent named FindPendingRequestIntent. This intent has no slots. Anything else?";
		this.emit(":ask", speechOutput, speechOutput);
    },
	'QueryPreviousRequestStatusIntent': function () {
		speechOutput = '';

		//any intent slot variables are listed here for convenience


		//Your custom intent handling goes here
		speechOutput = "This is a place holder response for the intent named QueryPreviousRequestStatusIntent. This intent has no slots. Anything else?";
		this.emit(":ask", speechOutput, speechOutput);
    },
	'QueryServerGroupSizeIntent': function () {
		speechOutput = '';

		//any intent slot variables are listed here for convenience


		//Your custom intent handling goes here
		speechOutput = "This is a place holder response for the intent named QueryServerGroupSizeIntent. This intent has no slots. Anything else?";
		this.emit(":ask", speechOutput, speechOutput);
    },
	'QueryResponseTimeIntent': function () {
		speechOutput = '';

		//any intent slot variables are listed here for convenience


		//Your custom intent handling goes here
		speechOutput = "This is a place holder response for the intent named QueryResponseTimeIntent. This intent has no slots. Anything else?";
		this.emit(":ask", speechOutput, speechOutput);
    },
	'QueryTransactionPerSecIntent': function () {
		speechOutput = '';

		//any intent slot variables are listed here for convenience


		//Your custom intent handling goes here
		speechOutput = "This is a place holder response for the intent named QueryTransactionPerSecIntent. This intent has no slots. Anything else?";
		this.emit(":ask", speechOutput, speechOutput);
    },
	'QueryVMInstancesParamIntent': function () {
		speechOutput = '';

		//any intent slot variables are listed here for convenience


		//Your custom intent handling goes here
		speechOutput = "This is a place holder response for the intent named QueryVMInstancesParamIntent. This intent has no slots. Anything else?";
		this.emit(":ask", speechOutput, speechOutput);
    },
    'QueryCpuUtilization': function () {
		speechOutput = '';

		//any intent slot variables are listed here for convenience
		let imageSlot = resolveCanonical(this.event.request.intent.slots.Image);
		console.log(imageSlot);
		let path = '/provision/hosts/query?host_tag='+imageSlot+'&param=cpu';
		  httpGet(path,  myResult => {
            console.log('myResult');
            console.log(myResult);
            this.response.speak(myResult).listen('try again');
            this.emit(':responseReady');

		});
    },
	'Unhandled': function () {
        speechOutput = "The skill didn't quite understand what you wanted.  Do you want to try something else?";
        this.emit(':ask', speechOutput, speechOutput);
    }
};


function httpGet(path, callback) {

    // Update these options with the details of the web service you would like to call
   	var options = {
		  host: '34.209.67.84',
		  port: 8080,
		  path: path
		};

    var req = http.request(options, res => {
        res.setEncoding('utf8');
        var returnData = "";

        res.on('data', chunk => {
            returnData = returnData + chunk;
        });

        res.on('end', () => {
            callback(returnData);  // this will execute whatever function the caller defined, with one argument

        });

    });
    req.end();

}


exports.handler = (event, context) => {
    const alexa = Alexa.handler(event, context);
    alexa.appId = APP_ID;
    // To enable string internationalization (i18n) features, set a resources object.
    //alexa.resources = languageStrings;
    alexa.registerHandlers(handlers);
	//alexa.dynamoDBTableName = 'DYNAMODB_TABLE_NAME'; //uncomment this line to save attributes to DB
    alexa.execute();
};

//    END of Intent Handlers {} ========================================================================================
// 3. Helper Function  =================================================================================================

function resolveCanonical(slot){
	//this function looks at the entity resolution part of request and returns the slot value if a synonyms is provided
	let canonical;
    try{
		canonical = slot.resolutions.resolutionsPerAuthority[0].values[0].value.name;
	}catch(err){
	    console.log(err.message);
	    canonical = slot.value;
	};
	return canonical;
};

function delegateSlotCollection(){
  console.log("in delegateSlotCollection");
  console.log("current dialogState: "+this.event.request.dialogState);
    if (this.event.request.dialogState === "STARTED") {
      console.log("in Beginning");
	  let updatedIntent= null;
	  // updatedIntent=this.event.request.intent;
      //optionally pre-fill slots: update the intent object with slot values for which
      //you have defaults, then return Dialog.Delegate with this updated intent
      // in the updatedIntent property
      //this.emit(":delegate", updatedIntent); //uncomment this is using ASK SDK 1.0.9 or newer

	  //this code is necessary if using ASK SDK versions prior to 1.0.9
	  if(this.isOverridden()) {
			return;
		}
		this.handler.response = buildSpeechletResponse({
			sessionAttributes: this.attributes,
			directives: getDialogDirectives('Dialog.Delegate', updatedIntent, null),
			shouldEndSession: false
		});
		this.emit(':responseReady', updatedIntent);

    } else if (this.event.request.dialogState !== "COMPLETED") {
      console.log("in not completed");
      // return a Dialog.Delegate directive with no updatedIntent property.
      //this.emit(":delegate"); //uncomment this is using ASK SDK 1.0.9 or newer

	  //this code necessary is using ASK SDK versions prior to 1.0.9
		if(this.isOverridden()) {
			return;
		}
		this.handler.response = buildSpeechletResponse({
			sessionAttributes: this.attributes,
			directives: getDialogDirectives('Dialog.Delegate', null, null),
			shouldEndSession: false
		});
		this.emit(':responseReady');

    } else {
      console.log("in completed");
      console.log("returning: "+ JSON.stringify(this.event.request.intent));
      // Dialog is now complete and all required slots should be filled,
      // so call your normal intent handler.
      return this.event.request.intent;
    }
}


function randomPhrase(array) {
    // the argument is an array [] of words or phrases
    let i = 0;
    i = Math.floor(Math.random() * array.length);
    return(array[i]);
}
function isSlotValid(request, slotName){
        let slot = request.intent.slots[slotName];
        //console.log("request = "+JSON.stringify(request)); //uncomment if you want to see the request
        let slotValue;

        //if we have a slot, get the text and store it into speechOutput
        if (slot && slot.value) {
            //we have a value in the slot
            slotValue = slot.value.toLowerCase();
            return slotValue;
        } else {
            //we didn't get a value in the slot.
            return false;
        }
}

//These functions are here to allow dialog directives to work with SDK versions prior to 1.0.9
//will be removed once Lambda templates are updated with the latest SDK

function createSpeechObject(optionsParam) {
    if (optionsParam && optionsParam.type === 'SSML') {
        return {
            type: optionsParam.type,
            ssml: optionsParam['speech']
        };
    } else {
        return {
            type: optionsParam.type || 'PlainText',
            text: optionsParam['speech'] || optionsParam
        };
    }
}

function buildSpeechletResponse(options) {
    let alexaResponse = {
        shouldEndSession: options.shouldEndSession
    };

    if (options.output) {
        alexaResponse.outputSpeech = createSpeechObject(options.output);
    }

    if (options.reprompt) {
        alexaResponse.reprompt = {
            outputSpeech: createSpeechObject(options.reprompt)
        };
    }

    if (options.directives) {
        alexaResponse.directives = options.directives;
    }

    if (options.cardTitle && options.cardContent) {
        alexaResponse.card = {
            type: 'Simple',
            title: options.cardTitle,
            content: options.cardContent
        };

        if(options.cardImage && (options.cardImage.smallImageUrl || options.cardImage.largeImageUrl)) {
            alexaResponse.card.type = 'Standard';
            alexaResponse.card['image'] = {};

            delete alexaResponse.card.content;
            alexaResponse.card.text = options.cardContent;

            if(options.cardImage.smallImageUrl) {
                alexaResponse.card.image['smallImageUrl'] = options.cardImage.smallImageUrl;
            }

            if(options.cardImage.largeImageUrl) {
                alexaResponse.card.image['largeImageUrl'] = options.cardImage.largeImageUrl;
            }
        }
    } else if (options.cardType === 'LinkAccount') {
        alexaResponse.card = {
            type: 'LinkAccount'
        };
    } else if (options.cardType === 'AskForPermissionsConsent') {
        alexaResponse.card = {
            type: 'AskForPermissionsConsent',
            permissions: options.permissions
        };
    }

    let returnResult = {
        version: '1.0',
        response: alexaResponse
    };

    if (options.sessionAttributes) {
        returnResult.sessionAttributes = options.sessionAttributes;
    }
    return returnResult;
}

function getDialogDirectives(dialogType, updatedIntent, slotName) {
    let directive = {
        type: dialogType
    };

    if (dialogType === 'Dialog.ElicitSlot') {
        directive.slotToElicit = slotName;
    } else if (dialogType === 'Dialog.ConfirmSlot') {
        directive.slotToConfirm = slotName;
    }

    if (updatedIntent) {
        directive.updatedIntent = updatedIntent;
    }
    return [directive];
}