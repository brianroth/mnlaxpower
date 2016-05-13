desc "This task is called by the Heroku scheduler add-on"
task :update_feed => :environment do
  puts "Updating feed..."
  UpdateFeedWorker.perform_async
  puts "done."
end