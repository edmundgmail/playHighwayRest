package com.lrs.highway

import javax.inject.Inject

import com.google.inject.Singleton
import com.lrs.daos.core.{DocumentDao, Repository}
import com.lrs.models.Road
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.IndexType

import scala.concurrent.Future

@Singleton
class HighwayRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) extends DocumentDao[Road](reactiveMongoApi) with Repository[Road]  {

  override def collectionName = "RoadTable"

  override def ensureIndexes: Future[Boolean] = ensureIndex(List("roadId" -> IndexType.Ascending), unique = true)

  ensureIndexes
}
