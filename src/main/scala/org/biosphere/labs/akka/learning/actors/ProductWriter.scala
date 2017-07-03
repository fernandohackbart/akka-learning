package org.biosphere.labs.akka.learning.actors

import akka.actor.{Actor, ActorLogging}
import org.biosphere.labs.akka.learning.domain.OperationOutcome.FAIL
import org.biosphere.labs.akka.learning.domain.{Product, ProductOperationRequest, ProductOperationResponse}
import com.datastax.driver.core.{Cluster, PreparedStatement, Session}
import org.biosphere.labs.akka.learning.utils.Keyspaces

class ProductWriter(cluster: Cluster) extends Actor with ActorLogging {

  ensureKeyspace()
  val session = cluster.connect(Keyspaces.akkaCassandra)
  createSchema()
  val preparedStatement = session.prepare("INSERT INTO product(id, brand, name) VALUES (?, ?, ?);")

  def receive = {
    case ProductOperationRequest(operation, product) =>
      log.info(s"ProductWriter operation ($operation) on ${product.brand}.${product.name}!")
      createSchema()
      saveProduct(product)
      val por = ProductOperationResponse("OK")
      sender() ! por
    case _ =>
      log.warning("ProductWriter received unknown message")
      sender() ! FAIL
  }

  def saveProduct(product: Product): Unit = {

    val uuid = java.util.UUID.randomUUID.toString
    log.warning(s"ProductWriter.createSchema() Inserting product PK ($uuid)")
    session.executeAsync(preparedStatement.bind(uuid,product.brand, product.name))
    log.warning("ProductWriter.createSchema() Product saved")
  }

  def ensureKeyspace(): Unit = {
    log.warning("ProductWriter.ensureKeyspace() Connecting")
    val sessionInitial = cluster.connect()
    log.warning("ProductWriter.ensureKeyspace() Creating keyspace")
    sessionInitial.execute(s"CREATE KEYSPACE IF NOT EXISTS ${Keyspaces.akkaCassandra} WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};")
    log.warning("ProductWriter.ensureKeyspace() Closing session")
    sessionInitial.close()
  }

  def createSchema(): Unit = {
    log.warning("ProductWriter.createSchema() Creating table")
    session.execute(
      """CREATE TABLE IF NOT EXISTS akkacassandra.product (
        id UUID PRIMARY KEY,
        brand text,
        name text
        );""")
    log.warning("ProductWriter.createSchema() Table created")
  }
}
