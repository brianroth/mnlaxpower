desc "This task is called by the Heroku scheduler add-on"
task :update_feed => :environment do
	puts "Updating division 13414"
	UpdateFeedWorker.perform_async 13414
	# UpdateFeedWorker.perform_async 12179
	# UpdateFeedWorker.perform_async 10141
	# UpdateFeedWorker.perform_async 8997 
	# UpdateFeedWorker.perform_async 6981 
	# UpdateFeedWorker.perform_async 5791 
	# UpdateFeedWorker.perform_async 4143 
	# UpdateFeedWorker.perform_async 3708 
	# UpdateFeedWorker.perform_async 3394 
	# UpdateFeedWorker.perform_async 2733 
	# UpdateFeedWorker.perform_async 1687 
  puts "done."
end