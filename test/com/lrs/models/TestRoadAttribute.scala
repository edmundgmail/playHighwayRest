package com.lrs.models

import com.lrs.logging.Logging
import com.lrs.utils.Testing
import play.api.Logger

import scala.io.Source
case class TestAttributeRecord(ATTRIBUTENAME: String, CATEGORYNAME: String, CAT_NO: String, ATT_NO:String, CODE: String, DESCRIPTION:String, CODESOURCE:String)

class TestRoadAttribute extends Testing with Logging{
  it("should parse the csv file"){
      val lines = Source.fromFile("csv/roadattributes.csv").getLines.drop(1)
    val records = lines.map(_.split(",")).map(r=>TestAttributeRecord(r(0), r(1), r(2), r(3), r(4), r(5), r(6))).toStream
    val recordMap = records.groupBy[(String, String)](r=> (r.CAT_NO,r.ATT_NO))
  }

}
