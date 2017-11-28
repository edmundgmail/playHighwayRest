package com.lrs.highway

import javax.inject.Inject

import com.google.inject.Singleton
import com.lrs.daos.core.{DocumentDao, Repository}
import com.lrs.models.{RoadFeature}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.IndexType

import scala.concurrent.Future

@Singleton
class RoadFeatureRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) extends DocumentDao[RoadFeature](reactiveMongoApi) with Repository[RoadFeature]  {

  override def collectionName = "RoadFeatureTable"

  override def ensureIndexes: Future[Boolean] = ensureIndex(List("roadId" -> IndexType.Ascending, "dir" -> IndexType.Ascending), unique = true)

  ensureIndexes
}
