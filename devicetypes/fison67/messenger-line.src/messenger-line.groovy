/**
 *  Messenger Line (v.0.0.3)
 *
 * MIT License
 *
 * Copyright (c) 2018 fison67@nate.com
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
*/
 
import groovy.json.JsonSlurper

metadata {
	definition (name: "Messenger Line", namespace: "fison67", author: "fison67") {
        capability "Speech Synthesis"
        capability "Actuator"
	}

	simulator {}

	preferences {
		input name: "_token", title:"Token" , type: "text", required: true 
	}

	tiles {
    	multiAttributeTile(name:"status", type: "generic", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.status", key: "PRIMARY_CONTROL") {
				attributeState("status", label:'${currentValue}', backgroundColor:"#00a0dc")
			}
		}
    }
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def speak(text){
	log.debug "Speak :" + text
	sendCommand(text)
}

def updated() {}

def sendCommand(text){

	def options = [
        uri: "https://notify-api.line.me/api/notify",
        headers: [
        "accept": "application/json",
        	'Authorization': 'Bearer ' + settings._token,
            'content-type': 'application/x-www-form-urlencoded'
      	],
        query: [
            "message": text.replace(">>", "\n")
        ]
    ]
    log.debug options
    try {
        httpPost(options) { resp ->
            if(resp.data.status == 200){
            	log.debug "Success to send Message"
            } else{
            	log.debug "Failed to send Message >> ${resp.data}"
            }
        }
    } catch (e) {
        log.debug "something went wrong: $e"
    }
}
