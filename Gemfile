source 'https://rubygems.org'
ruby "2.3.1"

gem 'rails', '4.2.6'
gem 'sass-rails', '~> 5.0'
gem 'uglifier', '>= 1.3.0'
gem 'jquery-rails'
gem 'puma'
gem 'sidekiq'
gem 'rest-client'

group :production do
  gem 'rails_12factor'
  gem 'pg'
end

group :development, :test do
  gem 'sqlite3'
  gem 'factory_girl_rails', :require => false
  gem "rspec-rails"
  gem 'pry'
  gem 'pry-nav'
  gem 'shoulda-matchers', '~> 3.1'
end
