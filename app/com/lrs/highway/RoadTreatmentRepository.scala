package com.lrs.highway

import javax.inject.Inject

import com.google.inject.Singleton
import com.lrs.daos.core.{DocumentDao, Repository}
import com.lrs.models.{RoadFeature, RoadTreatment}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.IndexType

import scala.concurrent.Future

@Singleton
class RoadTreatmentRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) extends DocumentDao[RoadTreatment](reactiveMongoApi) with Repository[RoadTreatment]  {

  override def collectionName = "RoadTreatmentTable"

  override def ensureIndexes: Future[Boolean] = ensureIndex(List("roadId" -> IndexType.Ascending, "dir" -> IndexType.Ascending), unique = true)

  ensureIndexes
}
