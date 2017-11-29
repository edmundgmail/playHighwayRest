package com.lrs.highway

import javax.inject.Inject

import controllers.AssetsBuilder
import play.api.http.DefaultHttpErrorHandler

class Static  @Inject()(errorHandler: DefaultHttpErrorHandler) extends AssetsBuilder(errorHandler)
