Rails.application.routes.draw do
  get 'seasons/:id' => 'seasons#show', as: :season
  get 'divisions/:id' => 'divisions#show', as: :division
  get 'teams/:id' => 'teams#show', as: :team
  get 'organizations' => 'organizations#index', as: :organizations
  get 'organizations/:id' => 'organizations#show', as: :organization

  root 'divisions#default'

  get '/faq', to: 'application#faq'

  get '*unmatched_route', to: 'application#render_404'
end
