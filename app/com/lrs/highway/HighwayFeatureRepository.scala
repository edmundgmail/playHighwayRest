package com.lrs.highway

import javax.inject.Inject

import com.google.inject.Singleton
import com.lrs.daos.core.{DocumentDao, Repository}
import com.lrs.models.Road
import com.lrs.models.RoadFeatures.RoadFeature
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.IndexType

import scala.concurrent.Future

@Singleton
class HighwayFeatureRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) extends DocumentDao[RoadFeature](reactiveMongoApi) with Repository[RoadFeature]  {

  override def collectionName = "RoadFeatureTable"

  override def ensureIndexes: Future[Boolean] = ensureIndex(List("roadId" -> IndexType.Ascending), unique = true)

  ensureIndexes
}
