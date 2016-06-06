FactoryGirl.define do
  factory :season do
    name { '2016' }
    cms_code { 4143 }
  end
  factory :division do
    name { 'U15-A' }
    cms_code { 95739 }
    season
  end
  factory :organization do
    name { 'Eagan' }
  end
  factory :team do
    organization
    name { 'Eagan' }
    cms_code { 1234 }
    division
  end
end
