package com.lrs.highway

import javax.inject.Inject

import com.google.inject.Singleton
import com.lrs.daos.core.{DocumentDao, Repository}
import com.lrs.models.{ReferencePoint, Road}
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.IndexType

import scala.concurrent.Future

@Singleton
class RoadRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) extends DocumentDao[Road](reactiveMongoApi) with Repository[Road]  {

  override def collectionName = "RoadTable"

  override def ensureIndexes: Future[Boolean] = ensureIndex(List("roadId" -> IndexType.Ascending), unique = true)

  ensureIndexes
}
