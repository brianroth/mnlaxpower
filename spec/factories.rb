FactoryGirl.define do
  factory :season do
    name { '2016' }
  end
  factory :division do
    name { 'U15-A' }
    season
  end
  factory :team do
    name { 'Eagan' }
    cms_code { 1234 }
    division
  end
end
