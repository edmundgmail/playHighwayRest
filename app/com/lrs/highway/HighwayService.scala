package com.lrs.highway

import javax.inject.{Inject, Singleton}

import com.lrs.models.Road

import scala.concurrent.Future
import scala.util.Try

@Singleton
class HighwayService @Inject()(repository: HighwayRepository) {

  def create(road: Road): Future[Try[Road]] = {
    repository.insert(road)
  }

}
