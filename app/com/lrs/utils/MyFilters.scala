package com.lrs.utils

/**
  * Created by eguo on 11/9/17.
  */
import javax.inject.Inject

import play.api.http.DefaultHttpFilters
import play.filters.cors.CORSFilter

class MyFilters @Inject() (corsFilter: CORSFilter)
  extends DefaultHttpFilters(corsFilter)