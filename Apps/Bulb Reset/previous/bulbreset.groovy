/**
 * Please Note: This app is NOT released under any open-source license.
 * Please be sure to read the license agreement before installing this code.
 *
 * Copyright 2019 Andrew Parker, Parker IT North East Limited
 *
 * This software package is created and licensed by Parker IT North East Limited. A United Kingdom, Limited Company.
 *
 * This software, along with associated elements, including but not limited to online and/or electronic documentation are
 * protected by international laws and treaties governing intellectual property rights.
 *
 * This software has been licensed to you. All rights are reserved. You may use and/or modify the software.
 * You may not sublicense or distribute this software or any modifications to third parties in any way.
 *
 * You may not distribute any part of this software without the author's express permission
 *
 * By downloading, installing, and/or executing this software you hereby agree to the terms and conditions set forth in the Software license agreement.
 * This agreement can be found on-line at: http://hubitat.uk/Software_License_Agreement.txt
 * 
 * Hubitat is the trademark and intellectual property of Hubitat Inc. 
 * Parker IT North East Limited has no formal or informal affiliations or relationships with Hubitat.
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License Agreement
 * for the specific language governing permissions and limitations under the License.
 *
 *-------------------------------------------------------------------------------------------------------------------
 *
 *
 *  Last Update: 18/06/2019
 *
 *

 *  V1.0.0 - POC
 */



definition(
    name: "Bulb Reset Tool",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Toggles a light or switch in a sequence to reset it.",
    category: "Convenience",

     
    iconUrl: "",
    iconX2Url: ""
)

preferences {
     page name: "mainPage", title: "", install: true, uninstall: true
    
}
    
    
 def mainPage() {
	dynamicPage(name: "mainPage") {  
    preCheck()
      section() {  
      input "deviceToReset", "enum", title: "Select Device Type", submitOnChange: true, options: deviceType()
      } 
      
    section() {
       
        input  "controlSwitch1", "capability.switch", title: "Trigger Switch",  required: true
        input "flashSwitch1", "capability.switch", title: "Outlet or Switch to toggle",   required: true
        }  
        if(deviceToReset == "Custom"){     
    section(){
	    input "numFlashes", "number", title: "This number of times", required: true, defaultValue: 5
        input "delay1", "number", title: "On for.. (Seconds)", required: true, defaultValue: 5
        input "delay2", "number", title: "Off for.. (Seconds)", required: true, defaultValue: 5
        input "startType", "bool", title: "Start on or off?", required: true, defaultValue: "off"
        
    }
        }
        
        section() {input "logLevel", "enum", title: "Set Logging Level", required:true, defaultValue: "DEBUG & INFO", options: ["NONE", "INFO", "DEBUG & INFO"]}
		section() {label title: "Enter a name for this automation", required: false}
    }
}



def deviceType(){
    listInput1 = [
"Custom",
"AduroSmart ERIA",
"Aeotec Z-Wave Bulb",
"Cree A19", 
"Ikea Tradfri",
"Innr",
"Lightify A19", 
"Lightify BR30", 
"Osram", 
"Sengled",
"Sylvania Ultra iQ BR30",
"Yeelight",
"Zigbee OnOff Controller"
]  
    return listInput1
}


def installed(){initialise()}
def updated(){initialise()}
def initialise(){
	version()
    preCheck()
    resetBtnName()
	subscribeNow()
	log.info "Initialised with settings: ${settings}"
	logCheck()	
}
def subscribeNow() {
	unsubscribe()
    subscribe(controlSwitch1, "switch.on", runNow)  
}
def runNow(evt){
    getTypeActions()
//    log.warn "in runnow"
    LOGDEBUG("Waiting a couple of seconds before starting routine...")
    pauseExecution(2000)
    LOGINFO( "Toggling now.. Please wait a short while..")
    flashNow()  
}
def flashNow() {
	if(state.num > 0){
	flashSwitch1.on()
    pauseExecution(state.myDelay1)
    flashSwitch1.off()
    state.num = (state.num -1)
	LOGDEBUG("Number of toggles to go = $state.num")
    pauseExecution(state.myDelay2)
    flashNow()
    }
        else{
            flashSwitch1.on()
            LOGINFO("FINISHED! - Leaving switched on...")}
        
	}
def getTypeActions(){    
    if(deviceToReset == "Custom"){
  //      log.warn "in custom"
    LOGDEBUG(deviceToReset)  
    state.myDelay1 = delay1 *1000 
    state.myDelay2 = delay2 *1000
    state.num = numFlashes
    state.start = startType1
    if(state.start == "on"){
    LOGDEBUG("Making sure the device is on to start")
    flashSwitch1.on()
    }
    if(state.start == "off"){
    LOGDEBUG("Making sure the device is off to start")
     flashSwitch1.off()
        }                              
    }
    
     
    if(deviceToReset == "AduroSmart ERIA"){
    LOGDEBUG("Device Type: " +deviceToReset)
    state.myDelay1 = 500
    state.myDelay2 = 500
    state.num = 10
    LOGDEBUG("Making sure the device is off to start")
    state.start = "off"
    flashSwitch1.off()
    }     
     
    if(deviceToReset == "Aeotec Z-Wave Bulb"){
    LOGDEBUG("Device Type: " +deviceToReset)
    state.myDelay1 = 500
    state.myDelay2 = 500
    state.num = 2
    LOGDEBUG("Making sure the device is off to start")
    state.start = "off"
    flashSwitch1.off()
    }     
    if(deviceToReset == "Cree A19"){
    LOGDEBUG("Device Type: " +deviceToReset)
    state.myDelay1 = 1000
    state.myDelay2 = 1000
    state.num = 4
    LOGDEBUG("Making sure the device is off to start")
    state.start = "off"
    flashSwitch1.off()
    }
    if(deviceToReset == "Ikea Tradfri"){
    LOGDEBUG("Device Type: " +deviceToReset)
    state.myDelay1 = 500
    state.myDelay2 = 1000
    state.num = 6
    }
    if(deviceToReset == "Innr"){
    LOGDEBUG("Device Type: " +deviceToReset)
    state.myDelay1 = 4000
    state.myDelay2 = 3000
    state.num = 6
    LOGDEBUG("Making sure the device is off to start")
    state.start = "off"
    flashSwitch1.off()
    }
    if(deviceToReset == "Lightify A19"){
    LOGDEBUG("Device Type: " +deviceToReset)
    state.myDelay1 = 1000
    state.myDelay2 = 3000
    state.num = 5
    LOGDEBUG("Making sure the device is off to start")
    state.start = "off"    
    flashSwitch1.off()
    }
    if(deviceToReset == "Lightify BR30"){
    LOGDEBUG("Device Type: " +deviceToReset)
    state.myDelay1 = 5000
    state.myDelay2 = 5000   
    state.num = 5
    LOGDEBUG("Making sure the device is off to start")
    state.start = "off"    
    flashSwitch1.off()
    }
    if(deviceToReset == "Osram"){
    LOGDEBUG("Device Type: " +deviceToReset)
    state.myDelay1 = 5000
    state.myDelay2 = 5000    
    state.num = 5
    LOGDEBUG("Making sure the device is off to start")
    state.start = "off"    
    flashSwitch1.off()
    }  
    if(deviceToReset == "Sengled"){
    LOGDEBUG("Device Type: " +deviceToReset)
    state.myDelay1 = 500
    state.myDelay2 = 500
    state.num = 10
    LOGDEBUG("Making sure the device is on to start")
    state.start = "on"    
    flashSwitch1.on()
    }
    if(deviceToReset == "Sylvania Ultra iQ BR30"){
    LOGDEBUG("Device Type: " +deviceToReset)
    state.myDelay1 = 1000
    state.myDelay2 = 3000    
    state.num = 8
    LOGDEBUG("Making sure the device is off to start")
    state.start = "off"    
    flashSwitch1.off()    
    }  
    if(deviceToReset == "Yeelight"){
    LOGDEBUG("Device Type: " +deviceToReset)
    state.myDelay1 = 2000
    state.myDelay2 = 2000    
    state.num = 5
    LOGDEBUG("Making sure the device is off to start")
    state.start = "off"    
    flashSwitch1.off()    
    }  
    if(deviceToReset == "Zigbee OnOff Controller"){
    LOGDEBUG("Device Type: " +deviceToReset)
    state.myDelay1 = 4000
    state.myDelay2 = 4000    
    state.num = 5
    LOGDEBUG("Making sure the device is off to start")
    state.start = "off"    
    flashSwitch1.off()    
    }  
    
    
    
    LOGDEBUG("Final numbers - On for: $state.myDelay1 Milliseconds - Off for: $state.myDelay2 Milliseconds -  Number of Times: $state.num  - Stating state: $state.start")        
}
def version(){
	setDefaults()
    logCheck()
    resetBtnName()
    def random = new Random()
    Integer randomHour = random.nextInt(18-10) + 10
    Integer randomDayOfWeek = random.nextInt(7-1) + 1 // 1 to 7
    schedule("0 0 " + randomHour + " ? * " + randomDayOfWeek, updateCheck) 
    checkButtons()
}
def logsDown(){
    log.warn "Debug logging disabled... Info logging enabled"
    app.updateSetting("logLevel",[value:"INFO",type:"enum"])
	if(logLevel == "INFO") runIn(86400,logsDownAgain) 
}
def logsDownAgain(){
    log.warn "Info logging disabled."
    app.updateSetting("logLevel",[value:"NONE",type:"enum"])	
}
def logCheck(){
    state.checkLog = logLevel
	if(state.checkLog == "INFO"){log.info "Informational Logging Enabled"}
	if(state.checkLog == "DEBUG & INFO"){log.info "Debug & Info Logging Enabled"}
	if(state.checkLog == "NONE"){log.info "Further Logging Disabled"}
	if(logLevel == "DEBUG & INFO") runIn(1800,logsDown)
}
def LOGDEBUG(txt){
	if(state.checkLog == "DEBUG & INFO"){
    try {
    	log.debug("(App Version: ${state.version}) - ${txt}") 
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data! - DEBUG- ${txt}")
    }
  }		
}

def LOGINFO(txt){
	if(state.checkLog == "INFO" || state.checkLog == "DEBUG & INFO"){
    try {
    	log.info("(App Version: ${state.version}) - ${txt}") 
    } catch(ex) {
    	log.error("LOGINFO unable to output requested data! - INFO- ${txt}")
    }
  }
}



def display(){
    setDefaults()
    if(state.status){section(){paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}}
    if(state.status != "<b>** This app is no longer supported by $state.author  **</b>"){section(){input "updateBtn", "button", title: "$state.btnName"}}
    if(state.status != "Current"){section(){paragraph "<hr><b>Updated: </b><i>$state.Comment</i><br><br><i>Changes in version $state.newver</i><br>$state.UpdateInfo<hr><b>Update URL: </b><font color = 'red'> $state.updateURI</font><hr>"}}   
}
def checkButtons(){
    LOGDEBUG("Running checkButtons")
    appButtonHandler("updateBtn")
}
def appButtonHandler(btn){
    state.btnCall = btn
    if(state.btnCall == "updateBtn"){
    LOGDEBUG("Checking for updates now...")
    updateCheck()
    pause(3000)
    state.btnName = state.newBtn
    runIn(2, resetBtnName)
    }       
}   
def resetBtnName(){
    LOGDEBUG("Resetting Button")
    if(state.status != "Current"){
    state.btnName = state.newBtn
    }
    else{
    state.btnName = "Check For Update" 
    }
}    
def pushOverUpdate(inMsg){
    if(updateNotification == true){  
    newMessage = inMsg
    LOGDEBUG(" Message = $newMessage ")  
    state.msg1 = '[L]' + newMessage
    speakerUpdate.speak(state.msg1)
    }
}
def updateCheck(){
    setVersion()
    def paramsUD = [uri: "http://update.hubitat.uk/json/${state.CobraAppCheck}"]
    try {
    httpGet(paramsUD) { respUD ->
//  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code 
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def commentRead = (respUD.data.Comment)
       		state.Comment = commentRead

            def updateUri = (respUD.data.versions.UpdateInfo.GithubFiles.(state.InternalName))
            state.updateURI = updateUri   
            
            def newVerRaw = (respUD.data.versions.Application.(state.InternalName))
            state.newver = newVerRaw
            def newVer = (respUD.data.versions.Application.(state.InternalName).replace(".", ""))
       		def currentVer = state.version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Application.(state.InternalName))
                state.author = (respUD.data.author)
           
		if(newVer == "NLS"){
            state.status = "<b>** This app is no longer supported by $state.author  **</b>"  
             log.warn "** This app is no longer supported by $state.author **" 
            
      		}           
		else if(currentVer < newVer){
        	state.status = "<b>New Version Available ($newVerRaw)</b>"
        	log.warn "** There is a newer version of this app available  (Version: $newVerRaw) **"
        	log.warn " Update: $state.UpdateInfo "
             state.newBtn = state.status
            state.updateMsg = "There is a new version of '$state.ExternalName' available (Version: $newVerRaw)"
            
       		} 
		else if(currentVer > newVer){
        	state.status = "You are using a BETA ($state.version) - Release Version: $newVerRaw"
        	log.warn "** <b>$state.status</b>) **"
        	state.UpdateInfo = "N/A"
		}
		else{ 
      		state.status = "Current"
       		LOGDEBUG("You are using the current version of this app")
       		}
      					}
        	} 
        catch (e) {
        	log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
    		}
    if(state.status != "Current"){
		state.newBtn = state.status
		
        
    }
    else{
        state.newBtn = "No Update Available"
    }
        
        
}
def preCheck(){
	setVersion()
    state.appInstalled = app.getInstallationState()  
    if(state.appInstalled != 'COMPLETE'){
    section(){ paragraph "$state.preCheckMessage <br> <br> $state.agreementNotice"}
    }
    if(state.appInstalled == 'COMPLETE'){
    display()   
 	}
}
def setDefaults(){
    LOGDEBUG("Initialising defaults...")   
}
def setVersion(){
		state.version = "1.0.0"	 
		state.InternalName = "DeviceResetTool" 
    	state.ExternalName = "Bulb Reset"
		state.preCheckMessage = " This app is designed to toggle a device a number of times to reset it.<br><br> First, you need to create a virtual switch and set 'Enable auto off' for 1 second <br> Use that virtual switch as a 'trigger switch'<br><br> Then, select the device type and the outlet or switch that the device is connected to<br> By turning on the trigger switch, the app will toggle the outlet or switch using the correct sequence to reset the device"
		state.CobraAppCheck = "deviceresettool.json"
		state.agreementNotice = "<b>Please Note:</b><br> By downloading, installing, and/or executing this software you hereby agree to the terms and conditions set forth in the Software license agreement.<br> This agreement can be found on-line at: http://hubitat.uk/Software_License_Agreement.txt"
    	
}



























