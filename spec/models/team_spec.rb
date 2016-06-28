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
                                 division: division,
                                 start_date: Time.now)
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
                                 division: division,
                                 start_date: Time.now)

        eastview.home_games.create!(cms_code: 37218,
                                    location: 'Blackhawk Middle School',
                                    away_team: eagan,
                                    home_team_score: 0,
                                    away_team_score: 1,
                                    division: division,
                                    start_date: Time.now)
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
        expect(subject).to be 0.0
      end
    end
    context "team that hasn't played any games" do
      it 'computes correctly' do
        expect(subject).to be 0.0
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
    let(:eagan) { division.teams.create!(name: 'eagan', cms_code: 1) }
    let(:eastview) { division.teams.create!(name: 'eastview', cms_code: 2) }
    let(:lakeville) { division.teams.create!(name: 'lakeville', cms_code: 3) }
    let(:rosemount) { division.teams.create!(name: 'rosemount', cms_code: 4) }

    subject do
      eagan.owp
    end

    context 'team has no games' do
      it 'computes correctly' do
        expect(subject).to be 0.0
      end
    end
    context "team hasn't played any other games" do
      before do
        create_game(eagan, 0, eastview, 0)
      end
      it 'computes correctly' do
        expect(subject).to be 0.0
      end
    end
    context 'team has played one game' do
      before do
        create_game(eagan, 1, eastview, 1)
      end
      context "and lost" do
        before do
          create_game(eastview, 0, lakeville, 1)
        end
        it 'computes correctly' do
          expect(subject).to be 0.0
        end
      end
      context "and won" do
        before do
          create_game(eastview, 1, lakeville, 0)
        end
        it 'computes correctly' do
          expect(subject).to be 1.0
        end
      end
    end
    context 'team has played many games' do
      before do
        create_game(eagan, 6, rosemount, 5)
        create_game(eagan, 8, lakeville, 6)
        create_game(eastview, 1, eagan, 7)
        create_game(rosemount, 9, eagan, 6)
        create_game(lakeville, 8, eastview, 7)
        create_game(eastview, 5, rosemount, 6)
      end
      it 'computes correctly' do
        expect(subject).to be_within(0.1).of(0.75)
        expect(rosemount.owp).to be_within(0.1).of(0.66)
        expect(lakeville.owp).to be_within(0.1).of(0.33)
        expect(eastview.owp).to be_within(0.1).of(0.3889)
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
        create_game(eagan, 1, eastview, 1)
        create_game(lakeville, 0, eastview, 0)
      end
      it 'computes correctly' do
        expect(subject).to be 0
      end
    end
    context "team's opponent has played some games" do
      before do
        create_game(eagan, 1, eastview, 1)
        create_game(lakeville, 0, eastview, 1)
        create_game(rosemount, 0, eastview, 1)

        eastview.update_attributes(wins: 4, losses: 1, ties: 0)
        lakeville.update_attributes(wins: 5, losses: 0, ties: 0)
        rosemount.update_attributes(wins: 3, losses: 2, ties: 0)
      end
      it 'computes correctly' do
        expect(subject).to be 0.8
      end
    end
  end

  def create_game(home, home_score, away, away_score)
    @cms_code = (@cms_code || 0) + 1
    home.home_games.create!(cms_code: @cms_code,
                            location: 'loction',
                            away_team: away,
                            home_team_score: home_score,
                            away_team_score: away_score,
                            division: home.division,
                            start_date: Time.now)
  end
end
