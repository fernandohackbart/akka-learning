package org.biosphere.labs.akka.learning.utils

import com.datastax.driver.core.{Cluster, ProtocolOptions}
import com.typesafe.config.ConfigFactory

object Keyspaces {
  val akkaCassandra = "akka-learmning"
}

trait CassandraCluster {
  def cluster: Cluster
}

trait ConfigCassandraCluster extends CassandraCluster {
  private val config = ConfigFactory.load()
  private val cassandraConfig = config.getConfig("akka-cassandra.main.db.cassandra")
  private val port = cassandraConfig.getInt("port")
  //private val hosts = cassandraConfig.getStringList("hosts").toArray

  lazy val cluster: Cluster =
    Cluster.builder().
      //addContactPoints(hosts).
      addContactPoints("172.17.0.2").
      //withCompression(ProtocolOptions.Compression.SNAPPY).
      withPort(port).
      build()
}