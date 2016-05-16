require 'rails_helper'
require 'shared_examples_for_models'

describe Team do
  let(:name) { 'Eagan' }
  let(:cms_code) { 5656 }
  let(:season) { create :season }
  let(:division) { create :division }

  describe '#save' do
    subject do
      team = division.teams.create!(name: name, cms_code: cms_code)
    end

    it { is_expected.to have_attributes name: name }
    it { is_expected.to have_attributes cms_code: cms_code }
    it_behaves_like 'a newly created model instance'
  end

  describe '#games' do
    let(:eagan) { division.teams.create(name: 'Eagan', cms_code: 1234) }
    let(:eastview) { division.teams.create(name: 'Eastview', cms_code: 4321) }

    subject do
      eagan.games
    end

    context 'when no games' do
      it { should be_empty }
    end

    context 'with one home and no away games' do
      before do
       eagan.home_games.create!(cms_code: 38237,
        location: 'Northview',
        away_team: eastview,
        home_team_score: 1, 
        away_team_score: 0,
        date: DateTime.now)
      end

      it 'has one game' do
        expect(subject.count).to be 1
      end
    end

    context 'with one home and one away games' do
      before do
       eagan.home_games.create!(cms_code: 838,
        location: 'Northview',
        away_team: eastview,
        home_team_score: 1, 
        away_team_score: 0,
        date: DateTime.now)

       eastview.home_games.create!(cms_code: 37218,
        location: 'Blackhawk Middle School',
        away_team: eagan,
        home_team_score: 0, 
        away_team_score: 1,
        date: DateTime.now)
      end

      it 'has one game' do
        expect(subject.count).to be 2
      end
    end
  end

  describe '#errors' do
    subject do
      team.validate
      team.errors
    end

    context 'when validation passes' do
      let(:team) { division.teams.new(name: name, cms_code: 555) }
      it { should be_empty }
    end

    context 'when validation fails' do
      context 'when name is missing' do
        let(:team) { division.teams.new(cms_code: 555) }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:name, "can't be blank"]
        end
      end

      context 'when cms_code is missing' do
        let(:team) { division.teams.new(name: name) }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:cms_code, "can't be blank"]
        end
      end

      context 'when name is a duplicate' do
        let(:team) { division.teams.new(name: name, cms_code: 555) }

        before do
          division.teams.create(name: name, cms_code: 666)
        end

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:name, "has already been taken"]
        end
      end
    end
  end
end
