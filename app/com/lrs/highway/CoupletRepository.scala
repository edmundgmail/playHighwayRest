package com.lrs.highway

import javax.inject.Inject

import com.google.inject.Singleton
import com.lrs.daos.core.{DocumentDao, Repository}
import com.lrs.models.{Couplet, Project}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.IndexType

import scala.concurrent.Future

@Singleton
class CoupletRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) extends DocumentDao[Couplet](reactiveMongoApi) with Repository[Couplet]  {

  override def collectionName = "CoupletTable"

  override def ensureIndexes: Future[Boolean] = ensureIndex(List("coupletId" -> IndexType.Ascending), unique = true)

  ensureIndexes
}
