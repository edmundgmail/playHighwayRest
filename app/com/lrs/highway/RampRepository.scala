package com.lrs.highway

import javax.inject.Inject

import com.google.inject.Singleton
import com.lrs.daos.core.{DocumentDao, Repository}
import com.lrs.models.{Couplet, Ramp}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.IndexType

import scala.concurrent.Future

@Singleton
class RampRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) extends DocumentDao[Ramp](reactiveMongoApi) with Repository[Ramp]  {

  override def collectionName = "RampTable"

  override def ensureIndexes: Future[Boolean] = ensureIndex(List("rampId" -> IndexType.Ascending), unique = true)

  ensureIndexes
}
