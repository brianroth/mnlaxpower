class UpdateFeedWorker
  include Sidekiq::Worker
  def perform
    logger.info("Work work work")
  end
end