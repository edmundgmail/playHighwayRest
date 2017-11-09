add a road

{
    "action" : "AddRoadRecord",
    "roadName" : "Hero High Way",
    "roadId" : 2,
    "dateTime" : "20170101 01:10:10",
    "mainDir" : "E",
    "jurisDictionType" : "state",
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
    "action" : "RemoveSegmentRecord",
    "dateTime" : "20170101 10:10:10",
    "roadName": "Hero High Way",
    "roadId" : 1,
    "dir" : "E",
    "startPoint" : {"rpName": "RP1", "offset":0.9}, "endPoint": {"rpName": "RP2", "offset": 1.3}
  }



#### Features
* Mongo DB Migration support to organize DB changes
* JSON Fixtures support for inserting fixture data for integration test
* Mongo DB integration test
