require 'rails_helper'
require 'shared_examples_for_models'

describe Division do
  let(:name) {'U15-A'}
  let(:season) { create :season }

  describe '#save' do
    subject do
      division = season.divisions.create!(name: name)
    end

    it { is_expected.to have_attributes name: name }
    it_behaves_like 'a newly created model instance'
  end

  describe '#errors' do
    subject do
      division.validate
      division.errors
    end

    context 'when validation passes' do
      let(:division) { season.divisions.new(name: name) }

      it { should be_empty }
    end

    context 'when validation fails' do
      context 'when season is missing' do
        let(:division) { Division.new(name: name) }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:season, "can't be blank"]
        end
      end

      context 'when name is missing' do
        let(:division) { season.divisions.new }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:name, "can't be blank"]
        end
      end

      context 'when name is a duplicate' do
        let(:division) { season.divisions.new(name: name) }

        before do
          season.divisions.create(name: name)
        end

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:name, "has already been taken"]
        end
      end
    end
  end
end
