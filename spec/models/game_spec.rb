require 'rails_helper'
require 'shared_examples_for_models'

describe Game do
  let(:northview) { 'Northview' }
  let(:season) { create :season }
  let(:division) { create :division }
  let(:eagan) { division.teams.create(name: 'Eagan', cms_code: 1234) }
  let(:eastview) { division.teams.create(name: 'Eastview', cms_code: 4321) }
  let(:cms_code) { 237437 }
  let(:commentary) { 'Awesome game' }

  describe '#save' do
    subject do
      eagan.home_games.create!(cms_code: cms_code,
                               location: northview,
                               away_team: eastview,
                               home_team_score: 1,
                               away_team_score: 0,
                               division: division,
                               start_date: Time.now,
                               commentary: commentary)
    end

    it { is_expected.to have_attributes cms_code: cms_code }
    it { is_expected.to have_attributes location: northview }
    it { is_expected.to have_attributes away_team: eastview }
    it { is_expected.to have_attributes home_team: eagan }
    it { is_expected.to have_attributes home_team_score: 1 }
    it { is_expected.to have_attributes away_team_score: 0 }
    it { is_expected.to have_attributes commentary: commentary }
    it_behaves_like 'a newly created model instance'
  end

  describe '#errors' do
    subject do
      game.validate
      game.errors
    end

    context 'when validation passes' do
      let(:game) {
        Game.create!(cms_code: cms_code,
                     location: northview,
                     home_team: eagan,
                     away_team: eastview,
                     home_team_score: 1,
                     away_team_score: 0,
                     division: division,
                     start_date: Time.now)
      }
      it { should be_empty }
    end

    context 'when validation fails' do
      context 'because location is missing' do
        let(:game) {
          Game.create(cms_code: cms_code,
                      home_team: eagan,
                      away_team: eastview,
                      home_team_score: 1,
                      away_team_score: 0,
                      division: division,
                      start_date: Time.now)
        }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:location, "can't be blank"]
        end
      end

      context 'because the date is missing' do
        let(:game) {
          Game.create(cms_code: cms_code,
                      location: northview,
                      home_team: eagan,
                      away_team: eastview,
                      home_team_score: 1,
                      away_team_score: 0,
                      division: division)
        }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:start_date, "can't be blank"]
        end
      end

      context 'because the home team is missing' do
        let(:game) {
          Game.create(cms_code: cms_code,
                      location: northview,
                      away_team: eastview,
                      home_team_score: 1,
                      away_team_score: 0,
                      division: division,
                      start_date: Time.now)
        }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:home_team, "can't be blank"]
        end
      end

      context 'because the away team is missing' do
        let(:game) {
          Game.create(cms_code: cms_code,
                      location: northview,
                      home_team: eagan,
                      home_team_score: 1,
                      away_team_score: 0,
                      division: division,
                      start_date: Time.now)
        }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:away_team, "can't be blank"]
        end
      end

      context 'because the home team score is missing' do
        let(:game) {
          Game.create(cms_code: cms_code,
                      location: northview,
                      home_team: eagan,
                      away_team: eastview,
                      away_team_score: 0,
                      division: division,
                      start_date: Time.now)
        }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:home_team_score, "is not a number"]
        end
      end

      context 'because the home team score is less than zero' do
        let(:game) {
          Game.create(cms_code: cms_code,
                      location: northview,
                      home_team: eagan,
                      away_team: eastview,
                      home_team_score: -1,
                      away_team_score: 0,
                      division: division,
                      start_date: Time.now)
        }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:home_team_score, "must be greater than or equal to 0"]
        end
      end

      context 'because the home team score is not a whole number' do
        let(:game) {
          Game.create(cms_code: cms_code,
                      location: northview,
                      home_team: eagan,
                      away_team: eastview,
                      home_team_score: 1.1,
                      away_team_score: 0,
                      division: division,
                      start_date: Time.now)
        }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:home_team_score, "must be an integer"]
        end
      end

      context 'because the away team score is missing' do
        let(:game) {
          Game.create(cms_code: cms_code,
                      location: northview,
                      home_team: eagan,
                      away_team: eastview,
                      home_team_score: 1,
                      division: division,
                      start_date: Time.now)
        }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:away_team_score, "is not a number"]
        end
      end

      context 'because the away team score is less than zero' do
        let(:game) {
          Game.create(cms_code: cms_code,
                      location: northview,
                      home_team: eagan,
                      away_team: eastview,
                      home_team_score: 1,
                      away_team_score: -1,
                      division: division,
                      start_date: Time.now)
        }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:away_team_score, "must be greater than or equal to 0"]
        end
      end

      context 'because the away team score is not a whole number' do
        let(:game) {
          Game.create(cms_code: cms_code,
                      location: northview,
                      home_team: eagan,
                      away_team: eastview,
                      home_team_score: 1,
                      away_team_score: 1.1,
                      division: division,
                      start_date: Time.now)
        }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:away_team_score, "must be an integer"]
        end
      end
    end
  end
end
