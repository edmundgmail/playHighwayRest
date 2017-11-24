add a road

{
    "action" : "AddRoadRecord",
    "roadName" : "Hero High Way",
    "roadId" : 1,
    "dateTime" : "20170101 01:10:10",
    "mainDir" : "E",
    "jurisdictionType" : "state",
    "ownerShip" : "public",
    "prefixCode" : "PRE",
    "routeNumber" :"routeNumber",
    "modifierCode":"modifier",
    "mainlineCode":"mainline",
    "routeTypeCode":"String",
    "routeOfficialName":"String",
	"routeFullName":"String",
	"routeAlternateName":"String",
	"beginPlace":"Toronto",
	"endPlace":"Montreal",
    "directions" : [
        {
            "dir" : "E",
            "segments" : [
                "1.8,RP1,2.1,RP2,1.0",
                "1.9,RP3,2.3,RP4,0.9,RP5,0.5"
            ]
        },
        {
            "dir" : "W",
            "segments" : [
                "2.9,RP1,1.1,RP2,3.0",
                "2.9,RP3,2.5,RP4,0.9,RP5,0.6"
            ]
        }
    ]
}


{
    "action" : "AddRoadRecord",
    "roadName" : "Hero High Way",
    "roadId" : 2,
    "dateTime" : "20170101 01:10:10",
    "mainDir" : "E",
    "jurisdictionType" : "state",
    "ownerShip" : "public",
    "prefixCode" : "PRE",
    "routeNumber" :"routeNumber",
    "modifierCode":"modifier",
    "mainlineCode":"mainline",
    "routeTypeCode":"String",
    "routeOfficialName":"String",
	"routeFullName":"String",
	"routeAlternateName":"String",
	"beginPlace":"Toronto",
	"endPlace":"Montreal",
    "directions" : [
        {
            "dir" : "E",
            "segments" : [
                "1.8,RP6,2.1,RP7,1.0",
                "1.9,RP8,2.3,RP9,0.9,RP10,0.5"
            ]
        },
        {
            "dir" : "W",
            "segments" : [
                "2.9,RP11,1.1,RP12,3.0",
                "2.9,RP13,2.5,RP14,0.9,RP15,0.6"
            ]
        }
    ]
}


  {
    "action" : "RemoveSegmentRecord",
    "dateTime" : "20170101 10:10:10",
    "roadName": "Hero High Way",
    "roadId" : 1,
    "dir" : "E",
    "startPoint" : {"rpName": "RP1", "offset":0.9}, "endPoint": {"rpName": "RP1", "offset": 1.3}
  }

{"roadId":3,"action":"RemoveSegmentRecord",
"dir":"E","startPoint":{"rpName":"RP7","offset":"1"},"endPoint":{"rpName":"RP8","offset":"2"}}

   {
      "action" : "AddSegmentRecord",
      "dateTime" : "20170101 11:10:10",
      "roadName": "Hero High Way",
      "roadId" : 1,
      "dir" : "E",
      "afterRP" : "RP1",
      "beforeRP": "RP2",
      "segment" : "0.7, RP6,2.5,RP7,1.9,RP8,0.3",
      "leftConnect" : true,
      "rightConnect": true
    }

       {
          "action" : "UpdateLaneRecord",
          "dateTime" : "20170101 11:10:10",
          "roadId" : 1,
          "dir" : "E",
          "lane" : "RP1,-0.1,RP3,0.5,3"
        }

"1.8,RP1,2.1,RP2,1.0",
    {
      "action" : "TransferSegmentRecord",
      "dateTime" : "20170101 11:10:10",
      "fromRoadName": "Hero High Way",
      "roadId" : 1,
      "fromDir" : "E",
      "startPoint" : {"rpName": "RP1", "offset":0.9},
      "endPoint": {"rpName": "RP1", "offset": 1.3},
      "toRoadName" : "",
      "toRoadId" : 2,
      "toDir" : "E",
      "afterRP" : "RP1",
      "beforeRP": "RP3",
      "leftConnect" : true,
      "rightConnect": true
    }


{
	"projectId" : 1,
	"projectName" : "The Fantastic Project",
	"roadId" : 1,
	"projectType" : "Self Funded",
	"projectCode" : "RBCone",
	"federalNumber": "123",
	"stateNumber" : "456",
	"program" : "Intersting Program",
	"cost" : 123.456,
	"completionDate" : "20171116",
	"projectManager": "Edmund GUo"
}

add ramp
{"fromPoint":{"name":"point1","pointType":"SystemRoad","x":1,"y":2,"z":3,"roadId":3,"dir":"E","offset":1,"rp":{"name":" RP6","roadName":"Hero High Way","dir":"E","globalOffset":1.6,"distance":0,"x":0,"y":0,"z":0}},"toPoint":{"name":"point2","pointType":"NonSystemRoad","x":2,"y":3,"z":4},"rampName":"ramp1","rampId":3210871106,"length":3,"pavementType":"A","metered":"true"}

add couplet median
{"dateTime":"2017-11-30T05:00:00.000Z","coupletTpye":"Median","primary":{"roadId":3,"dir":"E","startRpName":"RP1","startOffset":"1","endRpName":" RP6","endOffset":"1"},"secondary":{"roadId":3,"dir":"W","startRpName":"RP1","startOffset":"1","endRpName":" RP6","endOffset":"1"}}

add project
{"roadId":3,"roadName":"Hero High Way","projectType":"projectTypeA","projectCode":"123","federalNumber":"123","stateNumber":"456","program":"program1","cost":111,"projectManager":"Eddie H","completionDate":"2017-11-24T01:43:54.979Z"}
#### Features
* Mongo DB Migration support to organize DB changes
* JSON Fixtures support for inserting fixture data for integration test
* Mongo DB integration test

