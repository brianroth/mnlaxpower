source 'https://rubygems.org'
ruby "2.3.1"

gem 'rails', '~> 5.0.0'
gem 'sass-rails', '~> 5.0'
gem 'uglifier', '>= 1.3.0'
gem 'jquery-rails'
gem 'puma', '~> 3.0'
gem 'sidekiq'
gem 'rest-client', '~> 2.0.0'

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
  gem 'rails-controller-testing'
end
