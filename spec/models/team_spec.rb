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
        date: '06-25-2015',
        time: '07:15 PM')
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
        date: '06-25-2015',
        time: '07:15 PM')

       eastview.home_games.create!(cms_code: 37218,
        location: 'Blackhawk Middle School',
        away_team: eagan,
        home_team_score: 0, 
        away_team_score: 1,
        date: '06-25-2015',
        time: '07:15 PM')
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

  describe '#wp' do
    let(:eagan) { division.teams.create!(name: 'Eagan', cms_code: 1) }

    subject do
      eagan.wp
    end

    context 'team with no games' do
      it 'computes correctly' do
        expect(subject).to be 0
      end
    end
    context "team that hasn't played any games" do
      it 'computes correctly' do
        expect(subject).to be 0
      end
    end
    context 'team has played one game' do
      context "and lost" do
        before do
          eagan.update_attributes(wins: 0, losses: 1, ties: 0)
        end
        it 'computes correctly' do
          expect(subject).to be 0.0
        end
      end
      context "and won" do
        before do
          eagan.update_attributes(wins: 1, losses: 0, ties: 0)
        end
        it 'computes correctly' do
          expect(subject).to be 1.0
        end
      end
    end
    context 'team has played many games' do
      before do
        eagan.update_attributes(wins: 2, losses: 2, ties: 2)
      end
      it 'computes correctly' do
        expect(subject).to be (1.0/3.0)
      end
    end
  end

  describe '#owp' do
    let(:eagan) { division.teams.create!(name: 'Eagan', cms_code: 1) }
    let(:eastview) { division.teams.create!(name: 'Eastview', cms_code: 2) }
    let(:lakeville) { division.teams.create!(name: 'Lakeville', cms_code: 3) }
    let(:rosemount) { division.teams.create!(name: 'Rosemount', cms_code: 4) }

    subject do
      eagan.owp
    end

    context 'team has no games' do
      it 'computes correctly' do
        expect(subject).to be 0
      end
    end
    context "team hasn't played any games" do
      before do
        eagan.home_games.create!(cms_code: '5367',
          location: 'Northview Lower Track',
          away_team: eastview,
          home_team_score: 0, 
          away_team_score: 0,
          date: '06-25-2016',
          time: '07:15 PM')
      end
      it 'computes correctly' do
        expect(subject).to be 0
      end
    end
    context 'team has played one game' do
      before do
        eagan.home_games.create!(cms_code: '5367',
          location: 'Northview Lower Track',
          away_team: eastview,
          home_team_score: 1, 
          away_team_score: 1,
          date: '06-25-2016',
          time: '07:15 PM')
      end
      context "and lost" do
        before do
          eastview.update_attributes(wins: 3, losses: 1, ties: 0)
        end
        it 'computes correctly' do
          expect(subject).to be 0.75
        end
      end
      context "and won" do
        before do
          eastview.update_attributes(wins: 1, losses: 3, ties: 0)
        end
        it 'computes correctly' do
          expect(subject).to be 0.25
        end
      end
    end
    context 'team has played many games' do
      before do
        eagan.home_games.create!(cms_code: '11',
          location: 'some field',
          away_team: eastview,
          home_team_score: 1, 
          away_team_score: 0,
          date: '06-25-2016',
          time: '07:15 PM')

        eagan.away_games.create!(cms_code: '12',
          location: 'some field',
          home_team: lakeville,
          home_team_score: 0, 
          away_team_score: 0,
          date: '06-26-2016',
          time: '07:15 PM')

        eagan.away_games.create!(cms_code: '13',
          location: 'some field',
          home_team: rosemount,
          home_team_score: 0, 
          away_team_score: 1,
          date: '06-27-2016',
          time: '07:15 PM')

          eastview.update_attributes(wins: 4, losses: 1, ties: 0)
          lakeville.update_attributes(wins: 5, losses: 0, ties: 0)
          rosemount.update_attributes(wins: 3, losses: 2, ties: 0)
      end
      it 'computes correctly' do
        expect(subject).to be 0.7
      end
    end
  end

  describe '#oowp' do
    let(:eagan) { division.teams.create!(name: 'Eagan', cms_code: 1) }
    let(:eastview) { division.teams.create!(name: 'Eastview', cms_code: 2) }
    let(:lakeville) { division.teams.create!(name: 'Lakeville', cms_code: 3) }
    let(:rosemount) { division.teams.create!(name: 'Rosemount', cms_code: 4) }

    subject do
      eagan.oowp
    end

    context 'team has no games' do
      it 'computes correctly' do
        expect(subject).to be 0
      end
    end
    context "team's opponent hasn't played any games" do
      before do
        eagan.home_games.create!(cms_code: '5367',
          location: 'Northview Lower Track',
          away_team: eastview,
          home_team_score: 1, 
          away_team_score: 1,
          date: '06-25-2016',
          time: '07:15 PM')

        eastview.away_games.create!(cms_code: '5367',
          location: 'Northview Lower Track',
          home_team: lakeville,
          home_team_score: 0, 
          away_team_score: 0,
          date: '06-25-2016',
          time: '07:15 PM')

        eastview.update_attributes(wins: 4, losses: 1, ties: 0)
        lakeville.update_attributes(wins: 5, losses: 0, ties: 0)
        rosemount.update_attributes(wins: 3, losses: 2, ties: 0)
      end
      it 'computes correctly' do
        expect(subject).to be 0
      end
    end
    context "team's opponent has played some games" do
      before do
        eagan.home_games.create!(cms_code: '123',
          location: 'Northview Lower Track',
          away_team: eastview,
          home_team_score: 1, 
          away_team_score: 1,
          date: '06-25-2016',
          time: '07:15 PM')

        eastview.away_games.create!(cms_code: '1234',
          location: 'Northview Lower Track',
          home_team: lakeville,
          home_team_score: 0, 
          away_team_score: 1,
          date: '06-25-2016',
          time: '07:15 PM')

        rosemount.home_games.create!(cms_code: '12346',
          location: 'Northview Lower Track',
          away_team: eastview,
          home_team_score: 0, 
          away_team_score: 1,
          date: '06-25-2016',
          time: '07:15 PM')

        eastview.update_attributes(wins: 4, losses: 1, ties: 0)
        lakeville.update_attributes(wins: 5, losses: 0, ties: 0)
        rosemount.update_attributes(wins: 3, losses: 2, ties: 0)
      end
      it 'computes correctly' do
        expect(subject).to be 0.8
      end
    end
  end
end
