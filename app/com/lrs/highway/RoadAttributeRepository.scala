package com.lrs.highway

import javax.inject.Inject

import com.google.inject.Singleton
import com.lrs.daos.core.{DocumentDao, Repository}
import com.lrs.models.{Couplet, RoadAttribute}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.IndexType

import scala.concurrent.Future

@Singleton
class RoadAttributeRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) extends DocumentDao[RoadAttribute](reactiveMongoApi) with Repository[RoadAttribute]  {

  override def collectionName = "RoadAttributeTable"

  override def ensureIndexes: Future[Boolean] = ensureIndex(List("categoryName"->IndexType.Ascending, "attributeName" -> IndexType.Ascending), unique = true)

  ensureIndexes
}
